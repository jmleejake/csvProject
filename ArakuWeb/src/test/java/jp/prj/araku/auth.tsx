import {
  createAsyncThunk,
  createSlice,
  PayloadAction,
  SerializedError,
} from "@reduxjs/toolkit";

import axios from "axios";
import { HYDRATE } from "next-redux-wrapper";
import { ModalSize } from "rsuite/cjs/Modal/utils";
import { ReactNode } from "react";
import CommonModal from "@components/modals/CommonModal";
import { isApp } from "@util/common_functions";
import { NextRouter } from "next/router";

export enum AuthStates {
  IDLE = "idle",
  LOADING = "loading",
}

export const login: any = createAsyncThunk(
  "auth/login",
  async (
    credentials: {
      "id-key": string;
      "secret-key": string;
      "login-url": string;
      "app-jwt-token": string;
      showModal: (
        content: ReactNode,
        size?: ModalSize | "full-screen",
        className?: string
      ) => void;
      closeModal: () => void;
      router: NextRouter;
      skipPasswordChange?: boolean;
      passwordChangeDelayYn?: string;
    },
    thunkAPI
  ) => {
    try {
      const params = new URLSearchParams();
      if (credentials["app-jwt-token"]) {
        params.append("app-jwt-token", credentials["app-jwt-token"]);
      }
      params.append("id-key", credentials["id-key"]);
      params.append("secret-key", credentials["secret-key"]);
      params.append("app-yn", isApp() ? "Y" : "N");

      const result = await axios.post("/api/v1/customer/info/check", null, {
        params: {
          type: "customerSeqAndPassword",
          value: credentials["id-key"],
          value2: credentials["secret-key"],
          appJwtToken: credentials["app-jwt-token"],
          appYn: isApp() ? "Y" : "N",
          passwordChangeDelayYn: credentials["passwordChangeDelayYn"] ?? "",
        },
        withCredentials: true,
      });

      if (result) {
        if (result.data.body.withdrawYn === "Y") {
          // 현재 날짜
          const currentDate = new Date();
          currentDate.setHours(0, 0, 0, 0);

          // 탈퇴 날짜
          const withdrawDate = new Date(result.data.body.withdrawDate);
          withdrawDate.setHours(0, 0, 0, 0);

          // 두 날짜 사이의 차이를 일 단위로 계산 (탈퇴 날짜 포함)
          const diffInDays =
            Math.floor(
              (currentDate.getTime() - withdrawDate.getTime()) /
                (1000 * 60 * 60 * 24)
            ) + 1; // +1을 해서 탈퇴 날짜를 포함

          // 5일 이내인 경우 모달 오픈
          if (diffInDays <= 5) {
            return new Promise<string | null>((resolve) => {
              credentials.showModal(
                <CommonModal
                  message={"탈퇴한 회원입니다. 탈퇴를 철회하시겠습니까?"}
                  type={"CONFIRM"}
                  closeModal={credentials.closeModal}
                  handleConfirmButton={() => {
                    (async () => {
                      try {
                        const res = await axios.post(
                          credentials["login-url"],
                          params,
                          {
                            withCredentials: true,
                          }
                        );

                        credentials.closeModal();
                        resolve(res.data.body.access_token);
                      } catch (error) {
                        credentials.closeModal();
                        resolve(null);
                      }
                    })();
                  }}
                />
              );
            });
          } else {
            return new Promise<string | null>((resolve) => {
              credentials.showModal(
                <CommonModal
                  message={"이미 탈퇴한 회원입니다. \n재가입 후 이용해주세요."}
                  type={"ALERT"}
                  closeModal={credentials.closeModal}
                  handleConfirmButton={null}
                />
              );
            });
          }
        } else {
          localStorage.setItem("id-key", result.data.body.customerId);
          localStorage.setItem("tmpPasswordYn", result.data.body.tmpPasswordYn);
        }
      }

      const res = await axios.post(credentials["login-url"], params, {
        withCredentials: true,
      });

      if (res?.data?.messages?.includes("PASSWORD_CHANGE_REQUIRED")) {
        return thunkAPI.rejectWithValue({
          reason: "PASSWORD_CHANGE_REQUIRED",
        });
      } else {
        return res.data.body.access_token;
      }
    } catch (error: any) {
      const errorMessage = "";
      if (error?.response?.status === 403) {
        // errorMessage = error?.response?.data?.message;
        // updateToast(id, "error", "아이디 또는 비밀번호를 확인해주세요.");
      } else if (error?.response?.status === 423) {
        // updateToast(id, "error", i18n?.t("common.invalid.account"));
      } else {
        // updateToast(
        //   id,
        //   "error",
        //   i18n?.t("common.contact.administrator") +
        //     "\nPlease contact the administrator."
        // );
      }
      return thunkAPI.rejectWithValue({ error: error.message });
    }
  }
);

export interface AuthSliceState {
  accessToken: string;
  loading: AuthStates;
  error?: SerializedError;
}

const internalInitialState = {
  accessToken: "",
  loading: AuthStates.IDLE,
  error: undefined,
};

export const authSlice = createSlice({
  name: "auth",
  initialState: internalInitialState,
  reducers: {
    updateAccessToken(
      state: AuthSliceState,
      action: PayloadAction<string>
    ): any {
      state.accessToken = action.payload;
    },
    authReset: () => internalInitialState,
  },
  extraReducers: (builder) => {
    builder.addCase(login.fulfilled, (state, action) => {
      state.accessToken = action.payload;
      state.loading = AuthStates.IDLE;
    });
    builder.addCase(login.rejected, (state, action: any) => {
      state.error = action.payload.error;
      state.loading = AuthStates.IDLE;
    });
    builder.addCase(HYDRATE, (state, action: any) => {
      if (action?.payload?.auth?.accessToken) {
        state.accessToken = action.payload.auth.accessToken;
        state.loading = AuthStates.IDLE;
      }
    });
  },
});

export const { updateAccessToken, authReset } = authSlice.actions;

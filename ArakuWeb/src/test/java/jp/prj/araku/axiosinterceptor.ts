// src/lib/customApi.ts
import axios from "axios";
import { getToast, sleep } from "@util/common_functions";
import JSONBigInt from "json-bigint";
import { store } from "@store/store";
import { updateAccessToken } from "@store/slices/auth";
const thekaryAcf = axios.create({
  baseURL: process.env.NEXT_PUBLIC_BASE_URL,
  timeout: 120000,
  withCredentials: true,
  transformResponse: (data) => {
    if (data) {
      try {
        return JSONBigInt({ useNativeBigInt: true }).parse(data);
      } catch (e) {
        // json 형태가 아니면 data 그대로 넘김.
        try {
          return JSON.parse(data);
        } catch (e) {
          return data;
        }
      }
    } else {
      return data;
    }
  },
  paramsSerializer: {
    indexes: null,
  },
});

thekaryAcf.interceptors.request.use(
  async (config) => {
    // axios url 가져오기
    const url = config.url;
    if (url?.startsWith("/api/v1/auth")) {
      let accessToken = store.getState().auth.accessToken;

      let refreshYn = false;

      // 리프레시 필요여부 체크
      try {
        if (accessToken) {
          const base64Payload = accessToken.split(".")[1];
          const payload = Buffer.from(base64Payload, "base64");
          const result = JSON.parse(payload.toString());
          const expireTime = result.exp * 1000;
          const currentTime = new Date().getTime();

          // 현재 시간 -30분 해서 만료일이 지난 토큰인지 체크
          if (currentTime + 30 * 60 * 1000 > expireTime) {
            refreshYn = true;
          }
        } else {
          // 토큰이 없을 경우
          refreshYn = true;
        }
      } catch (e) {
        refreshYn = true;
      }

      // 토큰 리프레시 시작
      try {
        if (refreshYn) {
          console.log("REFRESH Start!");
          let retryCnt = 0;
          while (retryCnt < 5) {
            try {
              const res = await axios.post("/api/v1/token/refresh", null, {
                withCredentials: true,
              });

              accessToken = res.data.body.access_token;
              store.dispatch(updateAccessToken(res.data.body.access_token));
              retryCnt = 5;
            } catch (error: any) {
              if (error?.response?.status == 500) {
                console.log("Retry authentication..." + retryCnt);
                if (retryCnt == 5) {
                  getToast(
                    "error",
                    "새로고침 후 다시 시도해주세요. 문제가 계속되면 관리자에게 문의해주세요.\nPlease refresh and try again. If the problem persists, please contact the administrator"
                  );
                } else {
                  await sleep(1000);
                }
              } else if (
                error?.response?.status == 401 ||
                error?.response?.status == 403
              ) {
                window.location.href =
                  "/customer/login?path=" + window.location.pathname;
              }
              retryCnt++;
            }
          }
        }
      } catch (e) {
        console.error(e);
      }

      if (config.headers && accessToken) {
        config.headers["Authorization"] = `Bearer ${accessToken}`;
        config.headers["FRONT-URL"] = window?.location?.pathname || "";
      }
    }

    return config;
  },
  (error) => Promise.reject(error)
);

thekaryAcf.interceptors.response.use(
  (res) => res,
  async (err) => {
    const {
      response: { status },
    } = err;
    if (status == 401) {
      try {
        const res = await axios.post("/api/v1/token/refresh", null, {
          withCredentials: true,
        });
        const accessToken = res.data.body.access_token;
        store.dispatch(updateAccessToken(accessToken));
        getToast(
          "error",
          "잠시 후 다시 시도해주세요.\nPlease try again later."
        );
        return Promise.reject(status);
      } catch (e: any) {
        if (e?.response?.status == 401 || e?.response?.status == 403) {
          document.body.innerHTML = "";
          const currentUrl = window.location.pathname;
          window.location.href = "/customer/login?path=" + currentUrl;
        }
      }
    } else {
      if (status == 403) {
        document.body.innerHTML = "";
        const currentUrl = window.location.pathname;
        window.location.href = "/customer/login?path=" + currentUrl;
      } else if (status == 422) {
        getToast("error", "권한이 없습니다.\nHave no authority");
      } else if (status >= 400 && status < 500) {
        if (err.response?.data?.messages) {
          getToast("error", err.response?.data?.messages);
        }
        return Promise.reject(err);
      } else if (err?.config?.url !== "/api/v1/auth/check") {
        if (process.env.NODE_ENV == "development") {
          getToast(
            "error",
            "새로고침 후 다시 시도해주세요.\nPlease refresh and try again."
          );
        }
      }
      return Promise.reject(status);
    }
  }
);
export default thekaryAcf;

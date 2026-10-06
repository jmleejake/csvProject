"use client";
import React, { useEffect, useState } from "react";
import { SlArrowRight } from "react-icons/sl";
import Link from "next/link";
import { useViewport } from "@components/common/ViewportProvider";
import { useRouter } from "next/router";
import * as yup from "yup";
import { SubmitHandler, useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { useAppDispatch, useAppSelector } from "@hoc/ReduxHoc";
import { login } from "@store/slices/auth";
import Breadcrumbs from "@components/common/Breadcrumbs";
import { getThekaryAcf } from "@util/common_api";
import { useQuery } from "@tanstack/react-query";
import { useSelector } from "react-redux";
import { RootState } from "@store/store";
import { useModal } from "@hoc/ModalHoc";
import { closeLoading, loadingToast } from "@util/common_functions";
import axios from "axios";

interface LoginValues {
  "id-key": string;
  "secret-key": string;
  "login-url": string;
  "app-jwt-token"?: string;
}

const initialValues: LoginValues = {
  "id-key": "",
  "secret-key": "",
  "login-url": "",
  "app-jwt-token": "",
};

function Index() {
  const schema = yup.object().shape({
    "id-key": yup.string().required("아이디 또는 비밀번호를 확인해주세요."),
    "secret-key": yup.string().required("아이디 또는 비밀번호를 확인해주세요."),
  });

  // const location = useLocation();
  // const queryString = location.search;

  const {
    register,
    handleSubmit,
    setValue,
    getValues,
    formState: { errors },
  } = useForm<LoginValues>({
    defaultValues: initialValues,
    resolver: yupResolver(schema),
  });

  const state = useSelector((state: RootState) => state);
  const { openModal, closeModal } = useModal();

  const [loginUrl, setLoginUrl] = useState("/api/v1/token/create");

  const { width } = useViewport();
  const breadcrumbs: Array<any> = [
    // 브레드 크럼 -> 홈을 제외한 경로 이름을 name 에, 경로를 path 에 입력
    // 예시 - 상위 depth 의 path 가 존재하지 않는 경우 또는 자기 자신의 경로인 경우 #
    // { depth: 1, name: "고객센터", path: "#" },
    // { depth: 2, name: "공지사항", path: "/cs/notice" },
    // { depth: 3, name: "공지사항 상세", path: "#"}
    { depth: 1, name: "로그인", path: "#" },
  ];

  const router = useRouter();
  const dispatch = useAppDispatch();

  // 로그인 실패 여부
  const [loginError, setLoginError] = useState({
    error: false,
    message: "",
  });

  const snsToken = useQuery<any>(
    ["SNS_TOKEN_INFO"],
    () => {
      const id = loadingToast();
      const data = getThekaryAcf("/api/v1/customer/setSession")
        .catch((err: any) => {
          console.log(err);
          return null;
        })
        .finally(() => closeLoading(id));
      return data;
    },
    {
      keepPreviousData: true,
      cacheTime: 0,
      staleTime: 0,
      refetchOnWindowFocus: false,
    }
  );

  const loginWithKakao = (param: string) => {
    const REST_API_KEY = process.env.NEXT_PUBLIC_KAKAO_REST_API_KEY;
    const REDIRECT_URI = process.env.NEXT_PUBLIC_KAKAO_REDIRECT_URI;
    const url = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code&state=${param}`;

    sessionStorage.setItem("socialType", "KAKAO");
    const callFrom = new URL(window.location.href).searchParams.get("callFrom");
    sessionStorage.setItem("callFrom", callFrom != null ? callFrom : "");

    router.push(url);
  };

  // const loginWithKakao = () => {
  //   const STATE = uuidv4();
  //   const url = `http://localhost:8681/api/v1/kakao/setSession?&state=${STATE}`;
  //   // `https://nid.naver.com/oauth2.0/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&state=${STATE}&response_type=code`;
  //
  //   router.push(url);
  // };

  const loginWithNaver = (param: string) => {
    const CLIENT_ID = process.env.NEXT_PUBLIC_NAVER_CLIENT_ID;
    const REDIRECT_URI = process.env.NEXT_PUBLIC_NAVER_REDIRECT_URI;
    const url = `https://nid.naver.com/oauth2.0/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&state=${param}&response_type=code`;
    sessionStorage.setItem("socialType", "NAVER");
    const callFrom = new URL(window.location.href).searchParams.get("callFrom");
    sessionStorage.setItem("callFrom", callFrom != null ? callFrom : "");
    router.push(url);
  };

  const loginWithGoogle = () => {
    const CLIENT_ID = process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID;
    const REDIRECT_URI = process.env.NEXT_PUBLIC_GOOGLE_REDIRECT_URI;
    const url = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email+https://www.googleapis.com/auth/userinfo.profile`;
    sessionStorage.setItem("socialType", "GOOGLE");
    const callFrom = new URL(window.location.href).searchParams.get("callFrom");
    sessionStorage.setItem("callFrom", callFrom != null ? callFrom : "");
    router.push(url);
  };

  const loginWithApple = (param: string) => {
    const CLIENT_ID = process.env.NEXT_PUBLIC_APPLE_CLIENT_ID;
    const SCOPE = process.env.NEXT_PUBLIC_APPLE_SCOPE;
    const REDIRECT_URI = process.env.NEXT_PUBLIC_APPLE_REDIRECT_URI;

    // response_type (required) → 응답 유형을 선택, "code" or "code id_token"
    // response_mode (required) → 응답 모드를 선택, "query" or "fragment" or "form_post"
    // scope → 로그인을 통해 확인하고 싶은 정보를 선택, "email" or "name" or "email name" (scope를 사용할 경우 response_mode는 form_post가 필수)
    const url = `https://appleid.apple.com/auth/authorize?state=${param}&response_type=code%20id_token&response_mode=form_post&scope=${SCOPE}&client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}`;
    sessionStorage.setItem("socialType", "APPLE");
    const callFrom = new URL(window.location.href).searchParams.get("callFrom");
    sessionStorage.setItem("callFrom", callFrom != null ? callFrom : "");
    router.push(url);
  };

  const onSubmit: SubmitHandler<LoginValues> = async (formData) => {
    formData["login-url"] = loginUrl;
    try {
      const result = await dispatch(
        login({
          ...formData,
          showModal: openModal,
          closeModal: closeModal,
          router: router,
        })
      );

      if (result?.payload?.reason === "PASSWORD_CHANGE_REQUIRED") {
        await router.push("/customer/info/modify/password");
        return;
      }

      if (result?.error?.message !== "Rejected") {
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        // 'path' 파라미터의 값을 가져옵니다.
        const path = urlParams?.get("path");

        await router.push(path ? path.toString() : "/");
      } else {
        setLoginError({
          error: true,
          message: "아이디 또는 비밀번호를 확인해주세요.",
        });
      }
    } catch (e) {
      console.log(e);
    }
  };

  const accessToken = useAppSelector((state) => state.auth.accessToken);
  useEffect(() => {
    axios
      .get("/api/v1/auth/check", {
        headers: {
          "Content-Type": `application/json`,
          Authorization: `Bearer ${accessToken}`,
        },
      })
      .then((data) => {
        // 상태값 200 확인
        if (data.status === 200) {
          // 메인페이지로 리다이렉트
          router.push("/");
        }
      })
      .catch((e) => {
        const data = localStorage.getItem("THEKARY_FIRST_LOAD");
        if (!data) {
          localStorage.setItem("THEKARY_FIRST_LOAD", "true");
          window.location.reload();
        } else {
          localStorage.removeItem("THEKARY_FIRST_LOAD");
        }
      });

    window.appBioLogin = async (appJwtToken: string) => {
      if (appJwtToken) {
        onSubmit({
          "id-key": "BIO_LOGIN",
          "secret-key": "BIO_LOGIN",
          "login-url": loginUrl,
          "app-jwt-token": appJwtToken,
        });
      }
    };
  }, []);
  return (
    <>
      <meta
        name="viewport"
        content="width=device-width, initial-scale=1, maximum-scale=1"
      />
      <section>
        <Breadcrumbs breadcrumbs={breadcrumbs} />
        <form
          onSubmit={(e) => {
            e.preventDefault();
          }}
        >
          <div className="subPage-topBanner">
            <h3>로그인</h3>
            <p>지금 로그인하시고 다양한 혜택을 확인해 보세요.</p>
          </div>
          <div className={"login_wrap main"}>
            {width < 992 && (
              <div className="login_login_wrap">
                <img src="/img/thekary_member_logo.svg" alt="logo" />
              </div>
            )}

            <div className={"login_input"}>
              <div className={"input_box"}>
                <input
                  type="text"
                  placeholder="아이디를 입력해주세요"
                  className={`input ${
                    errors["id-key"] || errors["secret-key"] ? "error" : ""
                  }`}
                  {...register("id-key")}
                ></input>
              </div>
              <div className={"input_box"}>
                <input
                  type="password"
                  placeholder="비밀번호를 입력해주세요"
                  className={"input"}
                  {...register("secret-key")}
                  onKeyDown={(e) => {
                    if (e.key === "Enter") {
                      if (snsToken.data) {
                        handleSubmit(onSubmit)(e);
                      }
                    }
                  }}
                ></input>
                {(errors["secret-key"] ||
                  errors["id-key"] ||
                  loginError.error) && (
                  <p className="login-errorMessage">
                    {errors["id-key"]?.message ||
                      errors["secret-key"]?.message ||
                      loginError.message}
                  </p>
                )}
              </div>
            </div>
            <div className="join_wrap">
              <div className={"login_footer"}>
                <div className={"save_btn d-flex"}>
                  <input
                    type={"checkbox"}
                    id={"save_checkbox"}
                    className={"save_checkbox"}
                  />
                  <label htmlFor={"save_checkbox"}>아이디 저장</label>
                </div>
                <div className={"login_footer_text d-flex"}>
                  <Link href={"/customer/find"}>
                    <span className={"cursor-point"} style={{ color: "black" }}>
                      아이디/비밀번호 찾기{" "}
                      <SlArrowRight style={{ fontSize: "0.6rem" }} />
                    </span>
                  </Link>
                </div>
              </div>
              <div className={"login_button"}>
                {width < 992 ? (
                  <button
                    onClick={(e) => {
                      if (snsToken.data) {
                        handleSubmit(onSubmit)(e);
                      }
                    }}
                    type={"submit"}
                    className={"btn large btn-bg-gray wd-100 btn-round font-lg"}
                  >
                    로그인
                  </button>
                ) : (
                  <button
                    onClick={(e) => {
                      if (snsToken.data) {
                        handleSubmit(onSubmit)(e);
                      }
                    }}
                    type={"button"}
                    className={"button"}
                  >
                    로그인
                  </button>
                )}
              </div>
              {width > 992 && (
                <div className={"join_btn_wrap"}>
                  <span>
                    회원 가입하고{" "}
                    <span className={"font-weight-bold"}>다양한 혜택</span>을
                    누리세요!
                  </span>
                  <span className={"join_btn"}>
                    <Link
                      className={"black"}
                      href="#"
                      onClick={() => {
                        const callFrom = new URL(
                          window.location.href
                        ).searchParams.get("callFrom");
                        sessionStorage.setItem(
                          "callFrom",
                          callFrom != null ? callFrom : ""
                        );
                        location.href = "/customer/join/certification";
                      }}
                    >
                      회원가입
                    </Link>
                  </span>
                </div>
              )}
            </div>
            <div className="sns_login_wrap">
              {width > 992 && (
                <div className={"sns_login_title"}>
                  <span>SNS 로그인</span>
                </div>
              )}
              <div className="sns_box default_mt_34 app-p-2 p-3rem">
                <button
                  type="button"
                  className="sns_login_btn naver_login_box"
                  onClick={() => {
                    if (snsToken.data) {
                      loginWithNaver(snsToken.data);
                    }
                  }}
                >
                  <img
                    className="icon_naver_logo"
                    src="/img/sns/icon_naver_logo.png"
                    alt="icon_naver_logo"
                  />
                </button>

                <button
                  type="button"
                  className="sns_login_btn kakao_login_box"
                  onClick={() => {
                    if (snsToken.data) {
                      loginWithKakao(snsToken.data);
                    }
                  }}
                >
                  <img
                    className="icon_kakao_logo"
                    src="/img/sns/icon_kakao_logo.png"
                    alt="icon_kakao_logo"
                  />
                </button>
                <button
                  type="button"
                  className="sns_login_btn apple_login_box"
                  onClick={() => {
                    if (snsToken.data) {
                      loginWithApple(snsToken.data);
                    }
                  }}
                >
                  <img
                    id="appleid-signin"
                    className="icon_apple_logo"
                    src="/img/sns/icon_apple_logo.png"
                    alt="icon_apple_logo"
                  />
                </button>

                <button
                  type="button"
                  className="sns_login_btn google_login_box"
                  onClick={() => {
                    if (snsToken.data) {
                      loginWithGoogle();
                    }
                  }}
                >
                  <img
                    className="icon_google_logo"
                    src="/img/sns/icon_google_logo.png"
                    alt="icon_google_logo"
                  />
                </button>
              </div>
            </div>
            {width < 992 && (
              <>
                <div className="wd-100">
                  <hr className="wd-80" />
                </div>
                <div className={"join_btn_wrap text-center"}>
                  <span className="text-gray-12">
                    THEKARY 통합멤버십 회원에게만 드리는
                    <br />
                    크리스마스 선물같은 행복한 혜택을 받으실 수 있습니다.
                  </span>
                </div>
                {snsToken.data && (
                  <Link
                    href="#"
                    onClick={() => {
                      const callFrom = new URL(
                        window.location.href
                      ).searchParams.get("callFrom");
                      sessionStorage.setItem(
                        "callFrom",
                        callFrom != null ? callFrom : ""
                      );
                      location.href = "/customer/join/certification";
                    }}
                    className="black"
                  >
                    <button type={"button"} className="join-btn">
                      회원가입
                    </button>
                  </Link>
                )}
              </>
            )}
          </div>
        </form>
      </section>
    </>
  );
}

export default Index;

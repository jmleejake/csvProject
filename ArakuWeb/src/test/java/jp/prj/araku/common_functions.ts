import { toast } from "react-toastify";
import { Id, TypeOptions } from "react-toastify/dist/types";
import { store } from "@store/store";
import thekaryAcf from "@util/axiosInterceptors";
import { i18n } from "next-i18next";
import { getThekaryAcf, getTableList } from "@util/common_api";

/**
 * 로딩 시 클릭 방지 이벤트 함수
 */
const blockEventHandler = (e: any) => {
  e.preventDefault();
};

/**
 * 모바일 우측 상단 메뉴 토글 함수
 */
export const changeBurgerMenu = (e: React.MouseEvent<HTMLDivElement>) => {
  e.stopPropagation();
  if (!document.body.classList.contains("is-menu-visible")) {
    document.body.classList.add("is-menu-visible");
    document.body.classList.add("fixed-body");
  } else {
    document.body.classList.remove("is-menu-visible");
    document.body.classList.remove("fixed-body");
  }
};

/**
 * 공통 인증 헤더 함수 (JWT)
 */
export const getJwtHeader = () => {
  const accessToken = store.getState().auth.accessToken;

  return {
    Authorization: `Bearer ${accessToken}`,
  };
};

/**
 * (중요) 로그인 소비자 SEQ 가져오기
 */
export const getCustomerSeq = () => {
  try {
    const accessToken = store.getState().auth.accessToken;
    if (accessToken) {
      const base64Payload = accessToken.split(".")[1];
      const payload = Buffer.from(base64Payload, "base64");
      const result = JSON.parse(payload.toString());
      return result.sub;
    } else {
      return null;
    }
  } catch (e) {
    return null;
  }
};

/**
 * 메시지 공통함수
 */
const getMessage = (type: TypeOptions, message?: string | string[]) => {
  if (message) {
    if (Array.isArray(message)) {
      return message.join("\n");
    }
    return message;
  }
  if (type === "success") {
    return "성공하였습니다.";
  } else if (type === "error") {
    return "실패하였습니다.";
  }
};

/**
 * (중요) 화면에 알림창 메시지 띄우기
 */
export const getToast = (
  type: TypeOptions,
  message?: string | string[],
  autoCloseTime?: number
) => {
  toast(getMessage(type, message), {
    type: type,
    isLoading: false,
    position: "top-center",
    autoClose: autoCloseTime ? autoCloseTime : type === "success" ? 1000 : 5000,
    hideProgressBar: true,
    closeOnClick: true,
    rtl: false,
    pauseOnFocusLoss: true,
    draggable: true,
    pauseOnHover: true,
    theme: "light",
  });
};

/**
 * (중요) 화면에 로딩창 띄우기 (id 선언 후 updateToast와 closeLoading 함수로 제어 가능)
 */
export const loadingToast = (): Id => {
  document.body.classList.add("block-click");
  document.body.addEventListener("keydown", blockEventHandler);
  const id = toast.loading("Loading ...", { position: "top-center" });

  return id;
};

/**
 * (중요) 로딩을 포함하여 모든 알림창을 닫을 수 있음, 파라미터에 id가 있으면 해당 알림창만 닫음
 */
export const closeLoading = (id?: Id): void => {
  document.body.classList.remove("block-click");
  document.body.removeEventListener("keydown", blockEventHandler);
  id ? toast.dismiss(id) : toast.dismiss();
};

/**
 * (중요) loadingToast로 띄운 로딩창을 업데이트 하는 함수
 */
export const updateToast = (
  id: Id,
  type: TypeOptions,
  message?: string | string[],
  autoCloseTime?: number
) => {
  document.body.classList.remove("block-click");
  document.body.removeEventListener("keydown", blockEventHandler);
  toast.update(id, {
    render: () => getMessage(type, message),
    type: type,
    isLoading: false,
    position: "top-center",
    autoClose: autoCloseTime ? autoCloseTime : type === "success" ? 1000 : 5000,
    hideProgressBar: true,
    closeOnClick: true,
    rtl: false,
    pauseOnFocusLoss: true,
    draggable: true,
    pauseOnHover: true,
    theme: "light",
  });
};

/**
 * (중요) 숫자 3자리마다 콤마 찍기
 */
export function thousandsSeparator(d: number | string): any {
  if (d == 0 || d) {
    return d.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  } else {
    return d;
  }
}

// 빈객체인지 체크
export function isEmptyObj(obj: object) {
  if (obj.constructor === Object && Object.keys(obj).length === 0) {
    return true;
  }

  return false;
}

// 빈배열인치 체크
export function isEmptyArr(arr: any[]) {
  if (Array.isArray(arr) && arr.length === 0) {
    return true;
  }

  return false;
}

// 다음 주소 FullAddress 가져오기
export function getFullAddress(data: any): string {
  let fullAddress = data.address;
  let extraAddress = "";

  if (data.addressType === "R") {
    if (data.bname !== "") {
      extraAddress += data.bname;
    }
    if (data.buildingName !== "") {
      extraAddress +=
        extraAddress !== "" ? `, ${data.buildingName}` : data.buildingName;
    }
    fullAddress += extraAddress !== "" ? ` (${extraAddress})` : "";
  }

  return fullAddress;
}

/**
 * (중요) 현재 날짜와 시간을 가져오는 함수 (YYYYMMDD_HHMMSS)
 */
export function getCurrentDateTime() {
  const now = new Date();

  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, "0");
  const day = String(now.getDate()).padStart(2, "0");
  const hours = String(now.getHours()).padStart(2, "0");
  const minutes = String(now.getMinutes()).padStart(2, "0");
  const seconds = String(now.getSeconds()).padStart(2, "0");

  const formattedDateTime = `${year}${month}${day}_${hours}${minutes}${seconds}`;

  return formattedDateTime;
}

/**
 * (중요) month 리스트를 가져오는 함수
 */
export const getMonths = (year: string) => {
  const months = [];
  for (let i = 1; i <= 12; i++) {
    months.push(year + "-" + (i < 10 ? "0" + i : i));
  }
  return months;
};

/**
 * 날짜에 월을 더하는 함수
 */
export const addMonthsToDate = (dateStr: string, months: number) => {
  // 입력된 날짜 문자열을 Date 객체로 변환
  const date = new Date(dateStr);

  // 월을 더함
  date.setMonth(date.getMonth() + months);

  // 결과를 원하는 형식으로 반환 (YYYY-MM-DD)
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
};

/**
 * (중요) Date 타입의 날짜를 문자열로 변환하는 함수 (YYYY-MM-DD)
 */
export function getDateStr(myDate: any) {
  const year = myDate.getFullYear();
  let month = myDate.getMonth() + 1;
  let day = myDate.getDate();

  month = month < 10 ? "0" + String(month) : month;
  day = day < 10 ? "0" + String(day) : day;

  return year + "-" + month + "-" + day;
}

/**
 * (중요) 현재 날짜와 시간을 가져오는 함수 (YYYYMMDD_HHMMSS)
 */
export function getDateStrYYYY_MM_DDHHmmss(myDate: any) {
  const year = myDate.getFullYear();
  const month = String(myDate.getMonth() + 1).padStart(2, "0");
  const day = String(myDate.getDate()).padStart(2, "0");
  const hours = String(myDate.getHours()).padStart(2, "0");
  const minutes = String(myDate.getMinutes()).padStart(2, "0");
  const seconds = String(myDate.getSeconds()).padStart(2, "0");

  const formattedDateTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

  return formattedDateTime;
}

/**
 * (중요) Date 타입의 날짜를 문자열로 변환하는 함수 (YYYY-MM)
 */
export function getDateStrYYYY_MM(myDate: any) {
  const year = myDate.getFullYear();
  let month = myDate.getMonth() + 1;

  month = month < 10 ? "0" + String(month) : month;

  return year + "-" + month;
}

/**
 * (중요) Date 타입의 날짜를 문자열로 변환하는 함수 (YYMMDD)
 */
export function getDateStrYYMMDD(myDate: any) {
  let year = myDate.getFullYear();
  year = year.toString().substring(2, 4);
  let month = myDate.getMonth() + 1;
  let day = myDate.getDate();

  month = month < 10 ? "0" + String(month) : month;
  day = day < 10 ? "0" + String(day) : day;

  return year + month + day;
}

/**
 * (중요) 어제 날짜를 문자열로 변환하는 함수 (YYYY-MM-DD)
 */
export function getDateYesterdayStr() {
  const myDate: any = new Date();
  myDate.setDate(new Date().getDate() - 1);

  const year = myDate.getFullYear();
  let month = myDate.getMonth() + 1;
  let day = myDate.getDate();

  month = month < 10 ? "0" + String(month) : month;
  day = day < 10 ? "0" + String(day) : day;

  return year + "-" + month + "-" + day;
}

/**
 * (중요) YYYY-MM-DD 형식의 문자열을 Date 타입으로 변환하는 함수
 */
export function parseDate(dateString: string | null): Date | null {
  if (dateString) {
    const dateParts = dateString.split("-");
    const year = parseInt(dateParts[0]);
    const month = parseInt(dateParts[1]) - 1;
    if (dateParts[2]) {
      const day = parseInt(dateParts[2]);
      return new Date(year, month, day);
    } else {
      return new Date(year, month);
    }
  }
  return null;
}

export function getToday() {
  const d = new Date();
  return getDateStr(d);
}

export function getThisYear() {
  const d = new Date();
  const year = d.getFullYear();
  return year;
}

export function getThisMonth() {
  const d = new Date();
  const month = d.getMonth() + 1;
  return month;
}

export function getLastWeek() {
  const d = new Date();
  const dayOfMonth = d.getDate();
  d.setDate(dayOfMonth - 7);
  return getDateStr(d);
}

export function getNDay(day: number) {
  const d = new Date();
  const dayOfMonth = d.getDate();
  d.setDate(dayOfMonth - day);
  return getDateStr(d);
}

export function getLastMonth() {
  const d = new Date();
  const monthOfYear = d.getMonth();
  d.setMonth(monthOfYear - 1);
  return getDateStr(d);
}

export const getYearArray = () => {
  const y = [];
  for (let i = 2010; i < 2100; i++) {
    y.push(i);
  }
  return y;
};

export const getMonthArray = () => {
  const m = [];
  for (let i = 1; i <= 12; i++) {
    m.push(i);
  }
  return m;
};

// 시작되는 주의 시작일
export const getThisWeekStartDate = () => {
  const today = new Date();
  const dayOfWeek = today.getDay(); // 오늘의 요일 (0은 일요일, 1은 월요일, ..., 6은 토요일)

  // 이번 주의 시작일을 계산
  const startOfWeek = new Date(today);
  startOfWeek.setDate(today.getDate() - dayOfWeek + (dayOfWeek === 0 ? -6 : 1));

  return getDateStr(startOfWeek);
};

// 시작되는 달의 시작일
export const getThisMonthStartDate = () => {
  const d = new Date();
  const monthOfYear = d.getMonth();
  d.setMonth(monthOfYear, 1);
  return getDateStr(d);
};

/**
 * (중요) 파일 다운로드 함수 (S3 경로, 원본파일명, 저장파일명)
 */
export const fileDownload = (
  s3Path: any,
  originalFileName: any,
  fileName: any
) => {
  const reqData = {
    originalFileName: originalFileName,
    fileName: fileName,
    s3Path: s3Path,
  };
  const id = loadingToast();
  thekaryAcf({
    method: "POST",
    url: "/api/v1/file/download",
    data: JSON.stringify(reqData),
    headers: {
      "Content-Type": `application/json`,
    },
    responseType: "blob",
  })
    .then((res) => {
      const blob = new Blob([res.data]);
      blobFileDownload(blob, res.request);
      updateToast(id, "success"); // 성공
    })
    .catch((error) => {
      console.error(error);
      updateToast(id, "error"); // 에러표시
    });
};

/**
 * (중요) blob으로 response 온 파일을 다운로드 하는 함수
 */
export function blobFileDownload(blob: any, xhr: any) {
  // check for a filename
  let fileName = "";
  const disposition = xhr.getResponseHeader("Content-Disposition");

  if (disposition && disposition.indexOf("attachment") !== -1) {
    const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
    const matches = filenameRegex.exec(disposition);

    if (matches != null && matches[1]) {
      fileName = decodeURI(matches[1].replace(/['"]/g, ""));
    }
  }

  // for IE
  if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
    (window.navigator as any).msSaveOrOpenBlob(blob, fileName);
  } else {
    const URL = window.URL || window.webkitURL;
    const downloadUrl = URL.createObjectURL(blob);

    if (fileName) {
      const a = document.createElement("a");

      // for safari
      if (a.download === undefined) {
        window.location.href = downloadUrl;
      } else {
        a.href = downloadUrl;
        a.download = fileName;
        document.body.appendChild(a);
        a.click();
      }
    } else {
      window.location.href = downloadUrl;
    }
  }
}

/**
 * (중요) 물류 운송장을 출력하는 함수
 */
export const printTrackingNo = (data: any) => {
  if (confirm(i18n?.t("common.confirm.delivery.print"))) {
    const id = loadingToast();
    thekaryAcf({
      method: "POST",
      url: "/api/v1/logisticsShipping/trackingNo/print-exec",
      headers: {
        "Content-Type": `application/json`,
      },
      data: JSON.stringify(data),
      responseType: "blob",
    })
      .then((res) => {
        updateToast(id, "success"); // 성공
        if (res?.status === 200) {
          getToast("info", i18n?.t("common.popup.check"));
        }

        const file = new Blob([res.data], {
          type: "application/pdf",
        });

        const fileURL = URL.createObjectURL(file);
        window.open(
          fileURL,
          "shippingPDF",
          "width=1000, height=800, status=no, menubar=no, toolbar=no, resizable=no"
        );
      })
      .catch((error) => {
        console.error(error);
        if (error?.request?.status === 400) {
          getToast("error", i18n?.t("common.trackingNo.empty"));
        }
        updateToast(id, "error"); // 에러표시
      });
  }
};

/**
 * (중요) 물류에서 상품 택을 출력하는 함수
 */
export const printProductTag = (data: any) => {
  if (confirm(i18n?.t("common.confirm.tag.print"))) {
    const id = loadingToast();
    thekaryAcf({
      method: "POST",
      url: "/api/v1/product/tag/print-exec",
      headers: {
        "Content-Type": `application/json`,
      },
      data: JSON.stringify(data),
      responseType: "blob",
    })
      .then((res) => {
        updateToast(id, "success"); // 성공
        if (res?.status === 200) {
          getToast("info", i18n?.t("common.popup.check"));
        }

        const file = new Blob([res.data], {
          type: "application/pdf",
        });

        const fileURL = URL.createObjectURL(file);
        window.open(
          fileURL,
          "productTagPDF",
          "width=1000, height=800, status=no, menubar=no, toolbar=no, resizable=no"
        );
      })
      .catch((error) => {
        console.error(error);
        if (error?.request?.status === 400) {
          getToast("error", i18n?.t("common.service.error"));
        }
        updateToast(id, "error"); // 에러표시
      });
  }
};

/**
 * (중요) sleep 함수 (비동기 함수에서 사용) / ex) 1초 기다리고 싶을 때 : await sleep(1000);
 */
export const sleep = (ms: number) =>
  new Promise((resolve) => setTimeout(resolve, ms));

/**
 * MES 시스템 전용
 */
export const defaultWorkComments = () => {
  return (
    '<p style="line-height: 1;"><strong style="color: rgb(255, 0, 0);">공장</strong> 투입 전 확인 사항(봉제 공장)</p>' +
    '<p style="line-height: 1;"></p>'
  );
};

/**
 * MES 시스템 전용
 */
export const defaultWorkMainComments = () => {
  return (
    '<p style="line-height: 1;"><strong style="color: rgb(255, 0, 0);">뮤스타일 봉제 및 패턴 COMMENT</strong></p>' +
    '<p style="line-height: 0;"><strong style="fonts-size: 11px; color: rgb(255, 0, 0);">(메인작업시 문제될만한 것들은 모두 기재바랍니다.)</strong></p>' +
    '<p style="line-height: 1;"></p>'
  );
};

/**
 * MES 전용
 */
export const defaultWorkSampleTable = () => {
  return (
    '<p><strong style="color: rgb(255, 0, 0);">현재 샘플 90 (24M)</strong></p>' +
    '<table style="border-collapse: collapse; width: 101.516%; height: 236px;">' +
    "    <tbody>" +
    "        <tr>" +
    '            <td style="width: 16.6667%; text-align: center;"><strong>부위</strong></td>' +
    '            <td style="width: 16.6667%; text-align: center;"><strong>PATTERN<br>SPEC</strong></td>' +
    '            <td style="width: 16.6667%; text-align: center;"><strong>SAMPLE<br>SPEC</strong></td>' +
    '            <td style="width: 16.6667%; text-align: center;"><strong>1차 QC<br>SPEC</strong></td>' +
    '            <td style="width: 16.6667%; text-align: center;"><strong>MAIN<br>SPEC</strong></td>' +
    "        </tr>" +
    "        <tr>" +
    '            <td style="width: 16.6667%;">옆기장</td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    "        </tr>" +
    "        <tr>" +
    '            <td style="width: 16.6667%;">허리둘레</td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    "        </tr>" +
    "        <tr>" +
    '            <td style="width: 16.6667%;">앞밑위길이</td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    "        </tr>" +
    "        <tr>" +
    '            <td style="width: 16.6667%;">뒤밑위길이</td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    "        </tr>" +
    "        <tr>" +
    '            <td style="width: 16.6667%;">힙둘레</td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    "        </tr>" +
    "        <tr>" +
    '            <td style="width: 16.6667%;">밑단부리둘레</td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    "        </tr>" +
    "        <tr>" +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    "        </tr>" +
    "        <tr>" +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    "        </tr>" +
    "        <tr>" +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    '            <td style="width: 16.6667%;"><br></td>' +
    "        </tr>" +
    "    </tbody>" +
    "</table>"
  );
};

export const generateRandomString = (num: number) => {
  const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  let result = "";
  const charactersLength = characters.length;
  for (let i = 0; i < num; i++) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }

  return result;
};

/**
 * (중요) Set을 String으로 변환하는 함수 (구분값 ,)
 */
export const convertSetToString = (set: Set<string>): string => {
  const iterator = set[Symbol.iterator]();
  let resultString = "";
  let result = iterator.next();

  while (!result.done) {
    resultString += result.value
      ? (resultString ? "," : "") + result.value
      : "";
    result = iterator.next();
  }
  return resultString;
};

/**
 * dataList와 header & 파일 이름 받아서 엑셀 다운로드하는 함수
 */
export const excelDownload = (
  dataList: Array<Record<string, any>>,
  headerList?: Array<Array<string>>,
  fileName?: string
) => {
  // 만약 headerList가 있으면 dataList의 object 값을 headerList의 첫번째 값을 키로 맞춰서 정렬한다.
  let header = undefined;
  if (headerList) {
    dataList = dataList.map((data) => {
      const obj: Record<string, any> = {};
      headerList.forEach((header) => {
        if (header[0]) {
          obj[header[0]] = data[header[0]] ? data[header[0]] : "";
          if (data[header[0]] === 0) {
            obj[header[0]] = data[header[0]];
          }
        }
      });
      return obj;
    });
    header = headerList.map((header) => {
      if (header[0]) {
        return header[1];
      }
    });
  }

  const id = loadingToast();
  thekaryAcf({
    method: "POST",
    url: "/api/v1/common/excel/download-exec",
    data: JSON.stringify({
      headerList: header,
      dataList: dataList,
      fileName: fileName,
    }),
    responseType: "blob",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((res) => {
      const url = window.URL.createObjectURL(
        new Blob([res.data], { type: res.headers["content-type"] })
      );
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", fileName ? fileName : "file.xlsx");
      document.body.appendChild(link);
      link.click();
      updateToast(id, "success"); // 성공
    })
    .catch((error) => {
      updateToast(id, "error"); // 에러표시
    });
};

/**
 * (중요) 엑셀 템플릿 다운로드 함수
 */
export const excelTemplateDownload = (path: string) => {
  getThekaryAcf("/api/v1/common/source/excel", {
    path: path,
  }).then((res: any) => {
    const reqData = {
      originalFileName: res?.fileName,
      fileName: res?.fileName,
      s3Path: res?.s3Path,
    };
    const id = loadingToast(); // 로딩창 오픈

    thekaryAcf({
      method: "POST",
      url: "/api/v1/file/download",
      data: JSON.stringify(reqData),
      responseType: "blob",
      headers: {
        "Content-Type": `application/json`,
      },
    })
      .then((res) => {
        const file = new Blob([res.data]);
        blobFileDownload(file, res.request);
        updateToast(id, "success"); // 성공
      })
      .catch((error) => {
        console.error(error);
        updateToast(id, "error"); // 에러표시
      });
  });
};

/**
 * DataListModuled의 data와 columns와 파일이름을 받아서 엑셀 다운로드하는 함수
 *
 * (화면에 보이는 리스트만 다운로드)
 */
export const excelDownloadTable = (
  dataList: Array<Record<string, any>>,
  columns: Array<any>,
  fileName?: string
) => {
  const headerList: Array<Array<string>> = [];
  columns?.map((item: any) => {
    if (item?.accessor) {
      headerList.push([item?.accessor, item?.Header]);
    }
  });

  // 현재 날짜를 년, 월, 일로 포맷팅
  const currentDate = new Date();
  const year = currentDate.getFullYear();
  const month = (currentDate.getMonth() + 1).toString().padStart(2, "0");
  const day = currentDate.getDate().toString().padStart(2, "0");

  // 파일명에 년, 월, 일 추가
  const dateSuffix = `${year}${month}${day}`;

  // 파일명과 확장자 분리
  const fileNameParts = fileName?.split(".");
  const fileNameWithoutExtension = fileNameParts?.[0] || "download";
  const fileExtension = fileNameParts?.[1] || "xls";

  // 최종 파일명 설정
  const finalFileName = `${fileNameWithoutExtension}_${dateSuffix}.${fileExtension}`;

  excelDownload(dataList, headerList, finalFileName);
};

/**
 * DataListModuled의 data와 columns와 파일이름을 받아서 엑셀 다운로드하는 함수
 *
 * 화면에 보이지 않는 모든 리스트 다운로드 (최대 10만개)
 */
export const excelDownloadTableTotal = (
  columns: any,
  mainDataUrl: string,
  searchData?: any,
  fileName?: string,
  dataListCustomName?: any,
  totalCountCustomName?: any,
  downLoadRows?: number
) => {
  const id = loadingToast();
  (async () => {
    try {
      const downloadSize = downLoadRows ? downLoadRows : 100000;
      const dataList: any = [];
      const result: any = await getTableList(
        mainDataUrl,
        1,
        downloadSize,
        searchData
      );
      if (result) {
        const totalCnt = totalCountCustomName
          ? result[totalCountCustomName]
          : result.totalCount;

        // dataList 배열 뒤에 붙이기
        dataList.push(
          ...(dataListCustomName ? result[dataListCustomName] : result.dataList)
        );
        if (totalCnt > downloadSize) {
          const loopCount = Math.ceil(totalCnt / downloadSize);
          getToast(
            "info",
            `1/${loopCount} ${i18n?.t("common.complete")}\n\n${i18n
              ?.t("common.excel.bulk.message")
              .replace("{0}", thousandsSeparator(downloadSize))}`,
            20000
          );
          for (let i = 2; i <= loopCount; i++) {
            const result: any = await getTableList(
              mainDataUrl,
              i,
              downloadSize,
              searchData
            );
            getToast(
              "info",
              `${i}/${loopCount} ${i18n?.t("common.complete")}`,
              20000
            );
            dataList.push(
              ...(dataListCustomName
                ? result[dataListCustomName]
                : result.dataList)
            );
          }
        }
        // 로딩 닫기
        closeLoading(id);
        // 엑셀 다운로드
        excelDownloadTable(dataList, columns, fileName);
      } else {
        updateToast(id, "error");
      }
    } catch (e) {
      updateToast(id, "error");
      console.log(e);
    }
  })();
};

/**
 * 출고 검수에서 사용하는 사운드 실행
 */
export const playSound = (soundType: "SUCCESS" | "ERROR" | "ERROR2") => {
  if (soundType === "SUCCESS") {
    const audio = new Audio("/sounds/success.mp3"); // 성공 사운드 파일의 경로를 지정합니다.
    audio.play();
  } else if (soundType === "ERROR") {
    const audio = new Audio("/sounds/error.mp3"); // 에러 사운드 파일의 경로를 지정합니다.
    audio.play();
  } else if (soundType === "ERROR2") {
    const audio = new Audio("/sounds/error2.mp3"); // 에러 사운드 파일의 경로를 지정합니다.
    audio.play();
  }
};

/**
 * MPS의 혼용률 표시하는 함수
 */
export const mpsPlanIndexingCompositionInfo = (source: string) => {
  const temp = source.split("\r\n"); // 임시 리스트
  const typeList: string[] = []; // 분류 리스트
  const fiberList: string[] = []; // 소재 리스트
  const rateList: string[] = []; // 비율 리스트
  const newTypeList: string[] = []; // 인덱싱된 소재분류 리스트
  const result: string[] = [];

  // 분류, 소재, 비율로 리스트 분리
  temp.forEach((s: string) => {
    const splitList1 = s.split(": ");
    typeList.push(splitList1[0]);

    const splitList2 = splitList1[1].split(" ");
    rateList.push(
      splitList2
        .slice(0, splitList2.length - 1)
        .join(" ")
        .split("%")[0]
    );
    fiberList.push(splitList2[splitList2.length - 1]);
  });

  // 중복을 제거한 리스트 생성
  const typeSet = Array.from(new Set(typeList));
  // 중복 카운트 검사 리스트 : typeSet의 원소수 만큼 0으로 배열 초기화
  const duplicateCount = Array.from({ length: typeSet.length }, () => 0);

  // 중복된 수 만큼 카운트 업
  typeSet.forEach((setType: string, index: number) => {
    typeList.forEach((type: string) => {
      if (type == setType) {
        duplicateCount[index] = duplicateCount[index] + 1;
      }
    });
  });

  if (duplicateCount.length < duplicateCount.reduce((a, b) => a + b, 0)) {
    // 중복 카운트 만큼 인덱스 붙이기
    duplicateCount.forEach((count: number, index: number) => {
      let i = 1; // 인덱스 번호
      let beforRate = 0; // 비율
      if (count != 1) {
        typeList.forEach((type: string, tIndex: number) => {
          if (type == typeSet[index]) {
            beforRate = beforRate + Number(rateList[tIndex]);
            if (beforRate >= 100) {
              newTypeList.push(type + i.toString());
              i = i + 1;
              beforRate = 0;
            } else {
              newTypeList.push(type + i.toString());
            }
          }
        });
        // 혼용률 타입이 하나밖에 없을 때
        if (i == 1) {
          newTypeList.forEach((type: string, index: number) => {
            newTypeList[index] = type.replace("1", "");
          });
        }
      } else {
        newTypeList.push(typeSet[index]);
      }
    });
  } else {
    typeList.forEach((type: string) => {
      newTypeList.push(type);
    });
  }

  // 원래 문자열 대로 분류+소재 합치기
  newTypeList.forEach((type: string, index: number) => {
    result.push(type + ": " + rateList[index] + "%" + " " + fiberList[index]);
  });
  return result.join("\r\n");
};

/**
 * 포스 전체화면 설정
 */
export const openFullScreenMode = () => {
  const doc: any = document.documentElement;
  try {
    if (doc.requestFullscreen) {
      doc.requestFullscreen();
    } else if (doc.webkitRequestFullscreen) {
      // Chrome, Safari (webkit)
      doc.webkitRequestFullscreen();
    } else if (doc.mozRequestFullScreen) {
      // Firefox
      doc.mozRequestFullScreen();
    } else if (doc.msRequestFullscreen) {
      // IE or Edge
      doc.msRequestFullscreen();
    }
  } catch (e) {
    console.log(e);
  }
};

// 전체화면 해제
export const closeFullScreenMode = () => {
  const doc: any = document;
  try {
    if (doc.exitFullscreen) {
      doc.exitFullscreen();
    } else if (doc.webkitExitFullscreen) {
      // Chrome, Safari (webkit)
      doc.webkitExitFullscreen();
    } else if (doc.mozCancelFullScreen) {
      // Firefox
      doc.mozCancelFullScreen();
    } else if (doc.msExitFullscreen) {
      // IE or Edge
      doc.msExitFullscreen();
    }
  } catch (e) {
    console.log(e);
  }
};

/**
 * (중요) 대시보드에서 금액 값을 자동으로 라벨을 붙여주는 함수 (만원 단위, 억 단위)
 */
export const getPriceLabel = (price: number) => {
  if (typeof price !== "undefined") {
    const priceStr = Math.floor(price / 10000).toString();
    const resultPriceStr =
      (priceStr.length > 4
        ? priceStr.slice(0, priceStr.length - 4) +
          i18n?.t("common.hundred.million") +
          " " +
          priceStr.slice(priceStr.length - 4, priceStr.length)
        : priceStr) + i18n?.t("common.ten.thousand.won");
    const pattern = /0000만 원/g;
    if (resultPriceStr.indexOf("억") > -1) {
      return resultPriceStr.replace(pattern, `${i18n?.t("common.currency")}`);
    }
    return resultPriceStr.replace(pattern, "");
  }
  return price;
};
export const getPriceLargeLabel = (price: number) => {
  if (typeof price !== "undefined") {
    const priceStr = (Math.floor((price / 100000000) * 10) / 10)
      .toFixed(1)
      .toString();
    return (
      priceStr +
      i18n?.t("common.hundred.million") +
      " " +
      i18n?.t("common.currency")
    );
  }
  return price;
};

export const getCountMidiumLabel = (price: number) => {
  if (typeof price !== "undefined") {
    const priceStr = (Math.floor((price / 10000) * 10) / 10)
      .toFixed(1)
      .toString();
    return (
      priceStr +
      i18n?.t("common.tenThousandsWon") +
      " " +
      i18n?.t("common.count")
    );
  }
  return price;
};

/**
 * (중요) yyyy-mm-dd 형식인 birth로 만나이 가져오기
 */
export const getAge = (birth: string) => {
  if (birth) {
    // yyyy-mm-dd 형식인 birth로 만나이 가져오기
    const today = new Date();
    const birthDate = new Date(birth);
    let age = today.getFullYear() - birthDate.getFullYear();
    const month = today.getMonth() - birthDate.getMonth();
    if (month < 0 || (month === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    return age;
  } else {
    return null;
  }
};

/**
 * (중요) HTML 태그 내 스타일에 !important 추가하는 함수
 */
export const addImportantToStyles = (html: string) => {
  // 임시 div 요소 생성하여 HTML을 파싱
  const tempDiv = document.createElement("div");
  tempDiv.innerHTML = html;

  // 모든 요소에 대해 반복하여 스타일에 !important 추가
  const allElements = tempDiv.getElementsByTagName("*");
  for (let i = 0; i < allElements.length; i++) {
    const styles = allElements[i].getAttribute("style");
    if (styles) {
      // 각 스타일에 !important 추가
      const updatedStyles = styles
        .split(";")
        .map(function (style) {
          return style.trim() + " !important";
        })
        .join("; ");
      allElements[i].setAttribute("style", updatedStyles);
    }
  }

  // 수정된 HTML 반환
  return tempDiv.innerHTML;
};

/**
 * userId로 유저명 가져와서 붙이기
 * @param cell
 * @param userList
 */
export const addUserName = (cell: any, userList: any) => {
  if (
    cell?.column?.id &&
    ["userId", "resultId", "UserId", "designerId", "shippingUserId"].some(
      (keyword) => cell.column.id.includes(keyword)
    )
  ) {
    if (userList?.map((item: any) => item.userId).includes(cell?.value)) {
      const userName = userList?.filter(
        (item: any) => item.userId === cell?.value
      )[0]?.userName;
      if (userName) {
        return cell.value + " (" + userName + ")";
      }
    }
  }
  return cell.render("Cell");
};

/**
 * 일련번호 생성
 */
export const getSerialNo = (str?: string) => {
  const date = new Date();

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");
  const milliseconds = String(date.getMilliseconds()).padStart(3, "0");

  return `${str ? str : ""}${year}${month}${day}${hours}$
  
  {minutes}${seconds}${milliseconds}`;
};

/**
 * 앱 여부
 */
export const isApp = () => {
  try {
    const userAgent = navigator.userAgent;
    return userAgent.indexOf("THEKARY_POINT_APP") > -1;
  } catch (e) {
    return false;
  }
};

//PC 모바일 구분
export const isMobile = () => {
  return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(
    navigator.userAgent
  );
};

/**
 * 안드로이드/IOS 구분(운영체제)
 */
export const getDeviceOS = () => {
  if (typeof navigator === "undefined") {
    // navigator가 정의되지 않은 경우 (서버 사이드)
    return "unknown";
  }

  const userAgent = navigator.userAgent;

  // iOS인지 체크 (iPhone, iPad, iPod)
  if (/iPad|iPhone|iPod/.test(userAgent)) {
    return "ios";
  }

  // macOS인지 체크
  if (/Mac OS X|Macintosh/.test(userAgent)) {
    return "ios";
  }

  // Android인지 체크
  if (/android/i.test(userAgent)) {
    return "android";
  }

  // Windows인지 체크
  if (/Windows/.test(userAgent)) {
    return "windows";
  }

  return "unknown";
};

/**
 * 텍스트 길이 제한
 */
export const textLimit = (text: string, limit: number) => {
  if (text.length > limit) {
    return text.substring(0, limit) + "...";
  } else {
    return text;
  }
};

/**
 * 다음달 구하기
 */
export const getMonthAfterMonths = (months: number, suffix = ".") => {
  // 오늘 날짜 객체 생성
  const today = new Date();

  // 오늘 날짜에 주어진 개월 수를 더함
  const futureDate = new Date(today);
  futureDate.setMonth(today.getMonth() + months);

  // 결과 날짜를 yyyy.mm 형식으로 포맷
  const year = futureDate.getFullYear();
  const month = futureDate.getMonth() + 1; // 월은 0부터 시작하기 때문에 1을 더해줌
  const formattedMonth = month < 10 ? `0${month}` : month; // 한 자릿수 월을 두 자릿수로 포맷

  return `${year}.${formattedMonth}`;
  // 예시 사용법
  // const monthsToAdd = 6; // 6개월 후의 월을 구하고 싶을 때
  // const futureMonth = getMonthAfterMonths(monthsToAdd);
  // console.log(
  //   `오늘로부터 ${monthsToAdd}개월 후의 월은 ${futureMonth}월입니다.`
  // );
};

/**
 * date YYYYMMDD로 가져오기
 */
export const dateYmd = (date: string) => {
  if (date && date.length > 10) {
    return date.substring(0, 10);
  } else {
    return date;
  }
};

/**
 * 회원의 가입 유형(일반, 카카오, 네이버 등 소셜) 가져오기
 */
export const getCustomerRegistType = () => {
  try {
    const accessToken = store.getState().auth.accessToken;
    if (accessToken) {
      const base64Payload = accessToken.split(".")[1];
      const payload = Buffer.from(base64Payload, "base64");
      const result = JSON.parse(payload.toString());
      return result.registType;
    } else {
      return null;
    }
  } catch (e) {
    return null;
  }
};
export class UrlSchemeCaller {
  windowState = "visible";

  constructor() {
    this.init();
  }

  init() {
    document.addEventListener("visibilitychange", () => {
      if (document.visibilityState === "visible") {
        this.windowState = "visible";
      } else {
        this.windowState = "hidden";
      }
    });
  }

  call(urlScheme: string, notInstalledCallback: () => void) {
    location.href = urlScheme;

    setTimeout(() => {
      if (this.windowState === "visible") {
        // 앱이 설치되어 있지 않은 상태
        notInstalledCallback();
      }
    }, 300);
  }
}

// utils/base64url.ts
export function base64urlEncode(text: string): string {
  // UTF-8 → 바이너리
  const bytes = new TextEncoder().encode(text);
  let bin = "";
  for (let i = 0; i < bytes.length; i++) bin += String.fromCharCode(bytes[i]);

  // base64
  const b64 = btoa(bin);

  // URL-safe 변환(+ / = 제거)
  return b64.replace(/\+/g, "-").replace(/\//g, "_").replace(/=+$/g, "");
}

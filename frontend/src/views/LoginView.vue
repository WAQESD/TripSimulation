<script setup>
import { ref, onMounted } from "vue";
import { RouterView, useRouter } from "vue-router";
import { useModalStore } from "../stores/modal";
import axios from "axios";

import TheHeader from "../commons/TheHeader.vue";
import SimpleTextModal from "../components/modal/SimpleTextModal.vue";
import { useUserStore } from "../stores/user";

const router = useRouter();
const modalStore = useModalStore();
const userStore = useUserStore();

onMounted(() => {
  const script = document.createElement("script");
  script.src = "https://t1.kakaocdn.net/kakao_js_sdk/2.5.0/kakao.min.js";
  script.integrity = "sha384-kYPsUbBPlktXsY6/oNHSUDZoTX6+YI51f63jCPEIPFP09ttByAdxd2mEjKuhdqn4";
  script.crossOrigin = "anonymous";

  script.onload = () => {
    window.Kakao.init(import.meta.env.VITE_KAKAO_JAVASCRIPT_KEY);
  };
  document.head.appendChild(script);
});

const kakaoLogin = () => {
  window.Kakao.Auth.authorize({
    redirectUri: import.meta.env.VITE_KAKAO_REDIRECT_URL,
  });
};

const googleLogin = () => {
  const url =
    "https://accounts.google.com/o/oauth2/v2/auth?client_id=" +
    import.meta.env.VITE_GOOGLE_CLIENT_ID +
    "&redirect_uri=" +
    import.meta.env.VITE_GOOGLE_REDIRECT_URL +
    "&response_type=code" +
    "&scope=email profile";

  window.location.href = url;
};

const naverLogin = () => {
  const url =
    "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" +
    import.meta.env.VITE_NAVER_CLIENT_ID +
    "&redirect_uri=" +
    import.meta.env.VITE_NAVER_REDIRECT_URL +
    "&state=1234";

  window.location.href = url;
};

const userEmail = ref("");
const userPassword = ref("");

const login = () => {
  axios
    .post(import.meta.env.VITE_BASE_API + "/user/login", {
      userEmail: userEmail.value,
      password: userPassword.value,
    })
    .then(({ data }) => {
      localStorage.setItem("token", data.token);
      userStore.setUserInfo(data.userInfo);
      modalStore.setModal(true, SimpleTextModal, {
        text: "로그인에 성공했습니다.",
        callback: () => {
          router.go(-1);
        },
      });
    })
    .catch(() => {
      modalStore.setModal(true, SimpleTextModal, {
        text: "이메일 주소 또는 비밀번호가 틀렸습니다",
        callback: () => {
          router.go(0);
        },
      });
    });
};
</script>

<template>
  <main>
    <TheHeader></TheHeader>
    <div class="login-container">
      <div class="login-text-container">
        <h1>Log In</h1>
        <h3>
          회원가입 후 로그인하시면<br />
          Trippy의 더 다양한 기능들을<br />
          이용하실 수 있습니다.
        </h3>
      </div>
      <div class="car-animation-container">
        <div id="car-animation"></div>
      </div>
      <div class="login-form-container">
        <form class="login-form" @submit.prevent="login">
          <input
            type="email"
            id="login-email"
            name="login-email"
            placeholder="이메일 입력"
            v-model="userEmail"
            required
          />
          <input
            type="password"
            id="login-password"
            name="password"
            placeholder="비밀번호 입력"
            v-model="userPassword"
            required
          />
          <div class="login-remember-container">
            <input type="checkbox" id="login-remember" name="remember" />
            <label for="login-remember">로그인 상태 저장하기</label>
          </div>
          <button class="login-btn">로그인하기</button>
          <hr />
          <div class="social-login-btn-container">
            <div class="social-login-container naver" @click="naverLogin">
              <img class="social-login-btn" src="../assets/images/naver_circle.png" />
              <div>네이버 로그인</div>
            </div>
            <div class="social-login-container kakao" @click="kakaoLogin">
              <span class="social-login-btn-wrapper">
                <img class="social-login-btn kakao-btn" src="../assets/images/kakao_circle.png"
              /></span>
              <div>카카오 로그인</div>
            </div>
            <div class="social-login-container google" @click="googleLogin">
              <img class="social-login-btn" src="../assets/images/google_circle.svg" />
              <div>구글 로그인</div>
            </div>
          </div>
          <div class="account-service-container">
            <span class="clickable">비밀번호찾기</span><span>/</span
            ><span class="clickable" @click="router.push('/signup')">회원가입</span>
          </div>
        </form>
      </div>
    </div>
    <RouterView></RouterView>
  </main>
</template>

<style scoped>
main {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-grow: 1;
  background-color: #7c91ff;
}

h1 {
  margin-top: 0;
}

.clickable {
  cursor: pointer;
}
.login-container {
  width: 100%;
  flex-grow: 1;
  display: flex;
  position: relative;
}

#car-animation {
  font-size: 10px;
  --green: #7c91ff !important;
  height: 300px;
}
.car-animation-container {
  position: absolute;
  background: #7c91ff !important;
  width: 400px;
  height: 300px;
  bottom: 0;
}

.login-text-container {
  box-sizing: border-box;
  height: 100%;
  padding: 40px;
  color: white;
}
.login-form-container {
  margin-left: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: calc(100% - 400px);
  height: 100%;
  border: none;
  border-radius: 30px 0 0 0;
  padding-top: 30px;
  background-color: white;
  box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 29px 0px;
  box-sizing: border-box;
}

.login-form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

#login-email,
#login-password {
  width: 400px;
  height: 50px;
  border-radius: 10px;
  border: 0.5px grey solid;
  padding: 8px 20px;
  font-size: 18px;
  box-sizing: border-box;
}

input {
  margin: 6px 0;
  font-family: "Pretendard-Regular";
}

label {
  margin-left: 12px;
  cursor: pointer;
}

.login-remember-container {
  padding: 0 4px;
  margin: 10px 0 24px 0;
  width: 400px;
  box-sizing: border-box;
  display: flex;
}

.login-btn {
  width: 400px;
  height: 50px;
  border-radius: 10px;
  border: none;
  padding: 8px 20px;
  font-size: 18px;
  cursor: pointer;
  font-family: "Pretendard-Regular";
  box-sizing: border-box;
  background-color: #7583ff;
  color: white;
}

#login-remember {
  border-radius: 8px;
  margin: 0;
}

hr {
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  width: 440px;
  margin: 20px 0 20px 0;
}

h3 {
  font-weight: 300;
  line-height: 24px;
}

.social-login-btn {
  position: absolute;
  width: 50px;
  height: 50px;
  margin: 6px 0;
  cursor: pointer;
}

.kakao-btn {
  position: absolute;
  width: 30px;
  height: 30px;
  top: 5px;
  left: 10px;
}

.social-login-btn-wrapper {
  position: absolute;
  display: inline-block;
  width: 50px;
  height: 50px;
  background-color: #fee500;
  border-radius: 25px;
  margin: 6px 0;
}

.social-login-btn-container {
  width: 400px;
  display: flex;
  flex-direction: column;
}

.social-login-container {
  width: 400px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 12px;
  position: relative;
  cursor: pointer;
}

.account-service-container {
  display: flex;
  width: 300px;
  margin-top: 15px;
  justify-content: space-around;
}

.social-login-container div {
  margin: 0 auto;
}

.naver {
  color: white;
  background-color: #03c75a;
}

.kakao {
  background-color: #fee500;
}

.google {
  background-color: #f2f2f2;
}
</style>

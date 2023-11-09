<script setup>
import TheHeader from "../commons/TheHeader.vue";
import GradationBackground from "../components/GradationBackground.vue";
</script>

<template>
  <GradationBackground></GradationBackground>
  <TheHeader></TheHeader>
  <main>
    <div class="login-container">
      <h1>Login</h1>
      <form class="login-form" @submit.prevent>
        <input type="text" id="login-id" name="id" placeholder="아이디" required />
        <input type="password" id="login-password" name="password" placeholder="비밀번호" required />
        <div class="login-remember-container">
          <input type="checkbox" id="login-remember" name="remember" />
          <label for="login-remember">아이디 저장하기</label>
        </div>
        <button class="login-btn">로그인하기</button>
        <hr />
        <h2>소셜 로그인</h2>
        <div class="social-login-btn-container">
          <img class="social-login-btn" src="../assets/images/naver_circle.png" />
          <span class="social-login-btn-wrapper">
            <img class="social-login-btn kakao" src="../assets/images/kakao_circle.png"
          /></span>
          <img class="social-login-btn" src="../assets/images/google_circle.svg" />
        </div>
      </form>
    </div>
  </main>
</template>

<style scoped>
main {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-grow: 1;
  padding: 80px 0;
}

h1 {
  margin-top: 0;
}

.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 600px;
  border: 1px solid rgba(0, 0, 0, 0.2);
  border-radius: 20px;
  padding: 40px 0;
  background-color: white;
  box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 29px 0px;
}

.login-form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

#login-id,
#login-password {
  width: 240px;
  height: 25px;
  border-radius: 16px;
  border: 0.5px grey solid;
  padding: 8px 20px;
  font-size: 18px;
}

input {
  margin: 4px 0;
  font-family: "Pretendard-Regular";
}

label {
  margin-left: 12px;
  cursor: pointer;
}

.login-remember-container {
  padding: 4px;
  margin: 8px;
}

.login-btn {
  width: 280px;
  height: 45px;
  border-radius: 16px;
  border: none;
  padding: 8px 20px;
  font-size: 18px;
  cursor: pointer;
  font-family: "Pretendard-Regular";
}

#login-remember {
  border-radius: 8px;
}

hr {
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  width: 400px;
  margin: 20px 0 0 0;
}

.social-login-btn {
  width: 50px;
  height: 50px;
  margin: 6px 0;
  cursor: pointer;
}

.kakao {
  position: absolute;
  width: 30px;
  height: 30px;
  top: 5px;
  left: 10px;
}

.social-login-btn-wrapper {
  display: inline-block;
  position: relative;
  width: 50px;
  height: 50px;
  background-color: #fee500;
  border-radius: 25px;
  margin: 6px 0;
  cursor: pointer;
}

.social-login-btn-container {
  width: 200px;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
</style>

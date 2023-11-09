<script setup>
import GradationBackground from "../components/GradationBackground.vue";
import TheHeader from "../commons/TheHeader.vue";
import { ref } from "vue";

const agreementCertification = ref(false);

const toggleCertification = () => {
  agreementCertification.value = !agreementCertification.value;
};
</script>

<template>
  <GradationBackground></GradationBackground>
  <TheHeader></TheHeader>
  <main>
    <form class="signup-container">
      <div class="signup-userinfo-container">
        <input type="text" id="id" placeholder="아이디" required />
        <input type="password" id="password" placeholder="비밀번호" required />
        <input type="password" id="password-check" placeholder="비밀번호 확인" required />
        <input type="text" id="name" placeholder="이름" required />
        <div class="gender-radio-container">
          <div class="gender-radio-wrapper">
            <label for="gender-male">남성</label>
            <input type="radio" id="gender-male" name="gender" />
          </div>
          <div class="gender-radio-wrapper">
            <label for="gender-female">여성</label>
            <input type="radio" id="gender-female" name="gender" />
          </div>
        </div>
        <input type="date" id="birth" name="birth" />
      </div>
      <div class="signup-verify-container">
        <input type="email" id="email" placeholder="인증 이메일 입력" />
        <div class="signup-verify-wrapper">
          <div class="email-verify-timer">01:30</div>
          <input type="text" id="email-verify" placeholder="인증 번호 입력" />
        </div>
      </div>
      <button class="certification-btn" @click.prevent="toggleCertification">
        <input type="radio" :checked="agreementCertification" />
        <span>인증약관 동의</span>
      </button>
      <button class="signup-btn">인증요청</button>
    </form>
  </main>
</template>

<style scoped>
.signup-container,
.signup-userinfo-container,
.signup-verify-container {
  display: flex;
  flex-direction: column;
  margin: 0 auto;
  align-items: center;
  margin: 12px 0;
}

.signup-container {
  padding: 60px 0;
}

input,
.gender-radio-container {
  font-family: "Pretendard-Regular";
  font-size: 14px;
  box-sizing: border-box;
  padding: 4px 16px;
  margin: 0 6px;
  box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 29px 0px;
}

.gender-radio-container,
input[type="text"],
input[type="password"],
input[type="date"],
input[type="email"] {
  width: 320px;
  height: 40px;
  line-height: 32px;
  border: none;
  border-bottom: 0.5px grey solid;
  border-left: 0.5px grey solid;
  border-right: 0.5px grey solid;
}

.gender-radio-container {
  text-align: center;
  background-color: white;
  display: flex;
  justify-content: space-evenly;
}
.gender-radio-wrapper {
  display: flex;
  align-items: center;
}

input:first-child {
  border-top: 0.5px grey solid;
  border-radius: 12px 12px 0 0;
}

input:last-child {
  border-radius: 0 0 12px 12px;
}

.certification-btn {
  display: flex;
  align-items: center;
  font-family: "Pretendard-Regular";
  font-size: 16px;
  width: 320px;
  height: 60px;
  line-height: 60px;
  border: 0.5px grey solid;
  border-radius: 12px;
  background-color: white;
  padding: 0 20px;
  text-align: left;
  margin: 12px 0;
  cursor: pointer;
  box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 29px 0px;
}

.certification-btn > * {
  margin: 0 12px;
}

.signup-btn {
  font-family: "Pretendard-Regular";
  font-size: 16px;
  text-align: center;
  box-sizing: border-box;
  width: 240px;
  height: 40px;
  border: none;
  margin: 50px;
  border-radius: 12px;
  cursor: pointer;
  box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 29px 0px;
}

.signup-verify-wrapper {
  position: relative;
}

.email-verify-timer {
  color: rgba(0, 0, 0, 0.4);
  position: absolute;
  line-height: 40px;
  right: 20px;
}
</style>

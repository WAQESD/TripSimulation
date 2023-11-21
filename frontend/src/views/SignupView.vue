<script setup>
import { reactive } from "vue";

import TheHeader from "../commons/TheHeader.vue";

const userInfo = reactive({
  userEmail: "",
  userPwd: "",
  userName: "",
  birth: "",
  gender: "M",
  isForeign: false,
});

const setGender = (value) => {
  userInfo.gender = value;
};

const setForeign = (value) => {
  userInfo.isForeign = value;
};
</script>

<template>
  <main>
    <TheHeader></TheHeader>
    <div class="join-container">
      <div class="join-text-container">
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
      <div class="join-form-container">
        <form class="join-form" @submit.prevent="join">
          <input
            type="email"
            id="join-email"
            name="join-email"
            placeholder="이메일"
            v-model="userInfo.userEmail"
            required
          />
          <input
            type="password"
            id="join-password"
            name="password"
            placeholder="비밀번호"
            v-model="userInfo.userPwd"
            required
          />
          <input type="text" id="name" name="name" placeholder="이름" v-model="userInfo.userName" required />
          <input type="email" id="birth" name="birth" placeholder="생일" v-model="userEmail" required />
          <div class="radio-input-container">
            <div class="gender-input-container">
              <div
                type="radio"
                :class="{ selected: userInfo.gender === 'M' }"
                id="gender-male"
                @click="
                  () => {
                    setGender('M');
                  }
                "
              >
                남자
              </div>
              <div
                type="radio"
                :class="{ selected: userInfo.gender === 'F' }"
                id="gender-female"
                @click="
                  () => {
                    setGender('F');
                  }
                "
              >
                여자
              </div>
            </div>
            <div class="gender-input-container">
              <div
                id="not-foreign"
                :class="{ selected: userInfo.isForeign }"
                @click="
                  () => {
                    setForeign(true);
                  }
                "
              >
                내국인
              </div>
              <div
                id="foreign"
                :class="{ selected: !userInfo.isForeign }"
                @click="
                  () => {
                    setForeign(false);
                  }
                "
              >
                외국인
              </div>
            </div>
          </div>
          <button class="join-btn">JOIN</button>
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
.join-container {
  width: 100%;
  flex-grow: 1;
  display: flex;
  position: relative;
}

#car-animation {
  font-size: 10px;
  --green: #7c91ff !important;
}
.car-animation-container {
  position: absolute;
  background: #7c91ff !important;
  width: 400px;
  bottom: 0;
}

.join-text-container {
  box-sizing: border-box;
  height: 100%;
  padding: 40px;
  color: white;
}
.join-form-container {
  margin-left: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: calc(100% - 400px);
  height: 100%;
  border: none;
  border-radius: 30px 0 0 0;
  padding-top: 120px;
  background-color: white;
  box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 29px 0px;
  box-sizing: border-box;
}

.join-form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

#join-email,
#join-password,
#name,
#birth {
  width: 400px;
  height: 50px;
  border-radius: 10px;
  border: 0.5px grey solid;
  padding: 8px 20px;
  font-size: 18px;
  box-sizing: border-box;
}

input {
  margin: 12px 0;
  font-family: "Pretendard-Regular";
}

input:focus {
  border: 1px solid #8490ff;
}

label {
  margin-left: 12px;
  cursor: pointer;
}

.join-btn {
  width: 400px;
  height: 50px;
  border-radius: 10px;
  border: none;
  padding: 8px 20px;
  font-size: 18px;
  cursor: pointer;
  font-family: "Pretendard-Regular";
  box-sizing: border-box;
  background-color: #cbff5d;
  font-weight: bold;
}

h3 {
  font-weight: 300;
  line-height: 24px;
}

.radio-input-container {
  width: 400px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 10px 0 20px 0;
}

.gender-input-container {
  display: flex;
}

#gender-male,
#gender-female,
#not-foreign,
#foreign {
  width: 90px;
  height: 60px;
  line-height: 60px;
  text-align: center;
  box-sizing: border-box;
  border: 1px solid grey;
  border-radius: 10px;
  font-size: 15px;
  cursor: pointer;
}

#gender-male,
#not-foreign {
  border-radius: 10px 0 0 10px;
  border-right: none;
}

#gender-female,
#foreign {
  border-radius: 0 10px 10px 0;
}

.selected {
  background-color: #7583ff;
  color: white;
}
</style>

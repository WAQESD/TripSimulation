<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

import { useModalStore } from "../stores/modal";

import TheHeader from "../commons/TheHeader.vue";
import SimpleTextModal from "../components/modal/SimpleTextModal.vue";

const userInfo = reactive({
  userEmail: "",
  userPwd: "",
  userName: "",
  birth: "",
  gender: "M",
  isForeign: false,
});

const modalStore = useModalStore();
const router = useRouter();
const message = ref("이메일 인증이 필요합니다.");
const verifyCode = ref(null);
const verified = ref(false);
const authNumber = ref(0);
const idCheckResult = ref("");

const regist = () => {
  if (!verified.value) {
    alert("이메일 인증을 진행해주세요.");
    return;
  }
  axios
    .post(import.meta.env.VITE_BASE_API + "/user/regist", {
      userEmail: userInfo.userEmail,
      password: userInfo.userPwd,
      userName: userInfo.userName,
      birth: userInfo.birth,
      gender: userInfo.gender,
      isForeign: userInfo.isForeign,
    })
    .then(() => {
      modalStore.setModal(true, SimpleTextModal, {
        text: "회원가입에 성공했습니다.",
        callback: () => {
          router.push("/login");
        },
      });
    })
    .catch(() => {
      modalStore.setModal(true, SimpleTextModal, {
        text: "회원가입에 실패했습니다.",
        callback: () => {
          router.go(0);
        },
      });
    });
};

let timer = null;
const checkId = () => {
  if (timer) clearTimeout(timer);
  timer = setTimeout(() => {
    axios
      .get(import.meta.env.VITE_BASE_API + "/user/idcheck", { params: { userEmail: userInfo.userEmail } })
      .then(({ message }) => {
        idCheckResult.value = message;
      })
      .catch(({ message }) => {
        idCheckResult.value = message;
      });
    timer = null;
  }, 150);
};

const setGender = (value) => {
  userInfo.gender = value;
};

let sending = false;

const setForeign = (value) => {
  userInfo.isForeign = value;
};

const verify = () => {
  if (!userInfo.userEmail) message.value = "이메일을 입력해주세요.";
  else if (sending) message.value = "메일을 발송 중 입니다.";
  else {
    sending = true;
    if (authNumber.value) message.value = "메일을 다시 전송합니다.";
    axios.get(import.meta.env.VITE_BASE_API + "/send", { params: { email: userInfo.userEmail } }).then(({ data }) => {
      authNumber.value = data.authNumber;
      sending = false;
      message.value = "메일 전송이 완료되었습니다. 인증번호를 입력해주세요.";
    });
  }
};

const checkCode = () => {
  if (verifyCode.value == authNumber.value) {
    verified.value = true;
    message.value = "인증이 완료되었습니다.";
  }
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
          <div
            class="duplicated-email-indicator"
            :class="idCheckResult === '사용 가능한 이메일입니다.' ? 'blue' : 'red'"
          >
            {{ idCheckResult }}
          </div>
          <input
            type="email"
            id="join-email"
            name="join-email"
            placeholder="이메일"
            v-model="userInfo.userEmail"
            @keydown="checkId"
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
          <input type="date" id="birth" name="birth" placeholder="생일" v-model="userInfo.birth" required />
          <div class="email-check-container">
            <input
              class="email-check-input"
              name="code"
              v-model="verifyCode"
              type="text"
              @change="checkCode"
              :disabled="verified"
            />
            <div class="email-check-btn" @click="verify">코드 전송</div>
          </div>
          <div :class="verified ? 'verified' : 'not'">
            {{ message }}
          </div>
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
                :class="{ selected: !userInfo.isForeign }"
                @click="
                  () => {
                    setForeign(false);
                  }
                "
              >
                내국인
              </div>
              <div
                id="foreign"
                :class="{ selected: userInfo.isForeign }"
                @click="
                  () => {
                    setForeign(true);
                  }
                "
              >
                외국인
              </div>
            </div>
          </div>
          <button class="join-btn" type="button" @click="regist">JOIN</button>
        </form>
      </div>
    </div>
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
.duplicated-email-indicator {
  position: absolute;
  top: -24px;
}
.clickable {
  cursor: pointer;
}
.join-container {
  width: 100%;
  flex-grow: 1;
  display: flex;
}

.blue {
  color: rgba(0, 0, 255, 0.616);
}

.red {
  color: rgba(255, 0, 0, 0.608);
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
  padding-top: 60px;
  background-color: white;
  box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 29px 0px;
  box-sizing: border-box;
  position: relative;
}

.join-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
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

.email-check-container {
  display: flex;
  align-items: center;
}

.email-check-input {
  width: 320px;
  height: 50px;
  border-radius: 10px 0 0 10px;
  border: 0.5px grey solid;
  padding: 8px 20px;
  font-size: 18px;
  box-sizing: border-box;
}

.email-check-btn {
  width: 80px;
  height: 50px;
  line-height: 50px;
  background-color: #7583ff;
  cursor: pointer;
  border-radius: 0 10px 10px 0;
  border: 0.5px grey solid;
  border-left: none;
  box-sizing: border-box;
  color: white;
  text-align: center;
}

input {
  margin: 6px 0;
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

.not {
  color: rgba(255, 0, 0, 0.712);
}

.verified {
  color: rgba(0, 128, 0, 0.718);
}
</style>

<script setup>
import { ref } from "vue";
import TheHeader from "../commons/TheHeader.vue";

const pathList = ref([
  { title: "커피맛이 좋은 서울 카페투어 !", regDate: "2023.11.19" },
  { title: "커피맛이 좋은 서울 카페투어 !", regDate: "2023.11.19" },
  { title: "커피맛이 좋은 서울 카페투어 !", regDate: "2023.11.19" },
  { title: "커피맛이 좋은 서울 카페투어 !", regDate: "2023.11.19" },
  { title: "커피맛이 좋은 서울 카페투어 !", regDate: "2023.11.19" },
  { title: "커피맛이 좋은 서울 카페투어 !", regDate: "2023.11.19" },
  { title: "커피맛이 좋은 서울 카페투어 !", regDate: "2023.11.19" },
]);

const dateToString = (date) => {
  return new Intl.DateTimeFormat("kr").format(date);
};
</script>

<template>
  <TheHeader></TheHeader>
  <div class="my-page-container">
    <div class="user-info-container">
      <div class="user-info-image">
        <div class="user-info-image-indicator"></div>
      </div>
      <div class="user-info-text-container">
        <div class="user-info-text-nickname">soyoung님의 마이페이지</div>
        <div class="user-info-text-email">tiger6453127@naver.com</div>
        <div class="user-info-text-sperator"></div>
        <div class="user-info-text-birth">1998.05.15</div>
        <div class="user-info-text-mypage">MyPage</div>
      </div>
    </div>
    <div class="path-info-container">
      <div class="path-info-detail">
        <div id="path-info-detail-map"></div>
        <div class="path-info-detail-text">
          <div class="path-info-title-index">
            <span class="path-info-title"><span class="path-info-index">2</span>커피맛이 좋은 서울 카페투어 !</span>
          </div>
          <div class="path-info-waypoints">
            <div class="path-info-waypoints-title-list">
              <div class="path-info-waypoints-title">카페 경리단길</div>
              <div class="path-info-waypoints-title">카페 경리단길</div>
              <div class="path-info-waypoints-title">카페 경리단길</div>
              <div class="path-info-waypoints-title">카페 경리단길</div>
            </div>
            <div class="path-info-waypoints-time-list">
              <div class="path-info-waypoints-time">09:30</div>
              <div class="path-info-waypoints-time">10:30</div>
              <div class="path-info-waypoints-time">11:40</div>
              <div class="path-info-waypoints-time">12:50</div>
            </div>
          </div>
        </div>
      </div>
      <div class="path-info-list-container">
        <div class="path-info-list-mypath">My Path</div>
        <div class="path-info-list">
          <template v-for="(path, idx) in pathList" :key="path.title">
            <div class="path-info-list-item">
              <div class="path-info-item-index">{{ idx + 1 }}</div>
              <div class="path-info-item-title">{{ path.title }}</div>
              <div class="path-info-item-regdate">{{ path.regDate }}</div>
            </div>
          </template>
        </div>
      </div>
    </div>
    <div class="path-create-btn-container">
      <button class="path-create-btn">
        새 여행경로 만들기
        <div class="path-create-btn-icon"></div>
      </button>
    </div>
  </div>
</template>

<style scoped>
.my-page-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  box-sizing: border-box;
}

.user-info-container {
  display: flex;
  align-items: center;
  width: 1000px;
  box-sizing: border-box;
  height: 165px;
  background-color: #7c91ff;
  border-radius: 20px;
  padding: 0 50px;
}

.user-info-image {
  position: relative;
  width: 117px;
  height: 117px;
  border-radius: 60px;
  background-color: white;
  margin-right: 30px;
}

.user-info-image-indicator {
  position: absolute;
  width: 27px;
  height: 27px;
  background-color: #9dff6e;
  border-radius: 14px;
}

.user-info-text-container {
  display: flex;
  align-items: center;
  margin-bottom: 40px;
  flex-grow: 1;
  padding: 0;
  text-align: center;
  color: white;
  font-size: 16px;
}

.user-info-text-nickname {
  width: 200px;
  height: 48px;
  border-radius: 20px;
  border: 1px solid white;
  line-height: 48px;
  font-size: 16px;
}

.user-info-text-email {
  margin-left: 60px;
}

.user-info-text-sperator {
  height: 34px;
  border-left: 1px solid white;
  margin: 0 30px;
}

.user-info-text-mypage {
  margin-left: auto;
  font-size: 25px;
}

.path-info-container {
  display: flex;
  position: relative;
  margin-top: 35px;
}
.path-info-detail {
  width: 280px;
  height: 520px;
  box-shadow: 0px 4px 10.4px 0px rgba(0, 0, 0, 0.18);
  background-color: #f4f4f4;
  border-radius: 20px;
}

#path-info-detail-map {
  width: 280px;
  height: 241px;
  background-color: #d9d9d9;
  border-radius: 20px 20px 0 0;
}

.path-info-detail-text {
  text-align: center;
  margin-top: 30px;
}

.path-info-index {
  position: absolute;
  display: inline-block;
  left: -32px;
  top: -3px;
  width: 24px;
  height: 24px;
  line-height: 24px;
  border-radius: 12px;
  background-color: #adbaff;
}

.path-info-title {
  position: relative;
  line-height: 38px;
  font-size: 16px;
}

.path-info-waypoints {
  margin-top: 20px;
  display: flex;
  flex-grow: 1;
  padding: 0 30px;
  height: 150px;
  justify-content: center;
}

.path-info-waypoints-title-list {
  box-sizing: border-box;
  border-right: 1px solid #d0d0d0;
  padding-top: 15px;
}

.path-info-waypoints-time-list {
  box-sizing: border-box;
  padding: 20px;
  height: 200px;
  padding-top: 15px;
}

.path-info-waypoints-title {
  font-size: 14px;
  margin-bottom: 15px;
  margin-right: 20px;
}

.path-info-waypoints-time {
  font-size: 14px;
  margin-bottom: 15px;
}
.path-info-list-container {
  display: flex;
  flex-direction: column;
  position: relative;
  top: -86px;
  box-shadow: 0px 4px 10.4px 0px rgba(0, 0, 0, 0.18);
  width: 600px;
  height: 606px;
  margin-left: 30px;
  background-color: #f4f4f4;
  border-radius: 20px;
  padding: 0 34px;
  overflow-y: scroll;
  box-sizing: border-box;
}

.path-info-list-mypath {
  font-size: 25px;
  border-bottom: 1px solid #9a9a9a;
  padding: 12px 15px;
}

.path-info-list-item {
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 0 18px;
  width: 510px;
  height: 65px;
  border-radius: 20px;
  background-color: #e3e3e3;
  margin-top: 20px;
  margin-bottom: 20px;
}

.path-info-item-index {
  width: 38px;
  height: 38px;
  line-height: 38px;
  border-radius: 19px;
  text-align: center;
  background-color: #adbaff;
  font-size: 19px;
}

.path-info-item-title {
  margin-left: 25px;
}

.path-info-item-regdate {
  margin-left: auto;
}

.path-create-btn-container {
  width: 900px;
  display: flex;
  justify-content: flex-end;
}

.path-create-btn {
  width: 304px;
  height: 68px;
  box-sizing: border-box;
  background-color: #515151;
  border-radius: 25px;
  color: white;
  font-size: 18px;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  position: relative;
  line-height: 38px;
  margin-bottom: 40px;
  cursor: pointer;
  border: none;
}

.path-create-btn-icon {
  top: 15px;
  right: 15px;
  position: absolute;
  width: 38px;
  height: 38px;
  border-radius: 19px;
  background-color: #9dff6e;
}

.path-info-list-container::-webkit-scrollbar {
  width: 10px;
}

.path-info-list-container::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background-color: #b3ff8f;
}

.path-info-list-container::-webkit-scrollbar-button {
  width: 10px;
  height: 10px;
}
</style>

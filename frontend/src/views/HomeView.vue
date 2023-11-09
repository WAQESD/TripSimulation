<script setup>
import TheHeader from "../commons/TheHeader.vue";
import GradationBackground from "../components/GradationBackground.vue";
</script>

<template>
  <GradationBackground></GradationBackground>
  <TheHeader></TheHeader>
  <main>
    <img src="../assets/images/laptop.svg" alt="" />
    <div class="text-container">
      <div>
        <h2>Trippy</h2>
      </div>
      <div>
        <p>단순히 여행 경로를 계획하는 것이 아니라</p>
        <p>실시간으로 시뮬레이션하여 여행을 체험할 수 있습니다</p>
        <br />
        <p><strong>최적의 경로를 직접 찾아보세요</strong></p>
      </div>
      <button type="button" class="trip-btn">여행 시작하기</button>
    </div>
  </main>
</template>

<style scoped>
main {
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-around;
  flex-grow: 1;
}

p {
  text-align: center;
  font-size: 24px;
  margin: 12px 0 0 0;
}

h2 {
  font-family: "GmarketSansMedium";
  font-size: 84px;
  text-align: center;
  margin: 0;
}

.text-container {
  height: 400px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: cen;
}

.trip-btn {
  margin: 50px 0 10px 0;
  align-self: center;
  width: 254px;
  height: 64px;
  border-radius: 32px;
  font-family: "Pretendard-Regular";
  font-size: 20px;
  cursor: pointer;
  box-shadow: 0px 2px 0px black;
  position: relative;
}

.trip-btn:active {
  box-shadow: 0 0 black;
  top: 2px;
}
</style>

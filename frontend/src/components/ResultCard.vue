<script setup>
defineProps({
  place: Object,
  idx: Number,
});
</script>

<template>
  <div class="result-card-container">
    <div :class="idx % 2 === 0 ? 'right-box' : 'left-box'">
      <div :class="idx % 2 === 0 ? 'result-card-index-right' : 'result-card-index-left'">{{ idx }}</div>
      <div :class="idx % 2 === 0 ? 'result-card-circle-right' : 'result-card-circle-left'"></div>
    </div>
    <div class="result-card" :class="idx % 2 === 0 ? 'right' : 'left'">
      <img class="result-card-thumbnail" :src="place.thumbnail" width="177" height="177" />
      <div class="result-card-detail">
        <div class="result-card-title">{{ place.placeName }}</div>
        <div class="result-card-category">{{ place.category }}</div>
        <div class="result-card-description">{{ place.description }}</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.result-card-container {
  position: relative;
  width: 100vw;
  height: 250px;
  margin-top: 200px;
}

.result-card {
  position: absolute;
  display: flex;
  flex-direction: row;
  top: 70px;
  width: 500px;
  height: 177px;
  background-color: #f8f8f8;
  border-radius: 20px;
  box-sizing: border-box;
  box-shadow: 0px 4px 14.4px 0px rgba(0, 0, 0, 0.16);
}

.left {
  right: calc(50% + 130px);
}

.right {
  left: calc(50% + 130px);
}

.left-box,
.right-box {
  position: absolute;
  border-top: 1px #7c91ff solid;
  width: 418px;
  height: 50px;
  top: 0;
}

.left-box {
  right: 50%;
  border-left: 1px #7c91ff solid;
}

.right-box {
  left: 50%;
  border-right: 1px #7c91ff solid;
}

.result-card-index-right,
.result-card-index-left {
  box-sizing: border-box;
  width: 36px;
  height: 36px;
  line-height: 32px;
  border-radius: 18px;
  color: white;
  top: -18px;
  background-color: #7c91ff;
  border: 2px solid #9fffc0;
  text-align: center;
  position: absolute;
  font-size: 24px;
}
.result-card-index-left {
  right: -18px;
}

.result-card-index-right {
  left: -18px;
}

.result-card-circle-left,
.result-card-circle-right {
  position: absolute;
  width: 8px;
  height: 8px;
  bottom: -4px;
  border-radius: 4px;
  background-color: #7c91ff;
  box-sizing: border-box;
}

.result-card-circle-left {
  left: -4px;
}
.result-card-circle-right {
  right: -4px;
}

.result-card-thumbnail {
  border-radius: 20px 0 0 20px;
}

.result-card-detail {
  padding: 10px;
  display: flex;
  flex-direction: column;
}

.result-card-title {
  font-size: 20px;
}

.result-card-category {
  font-size: 12px;
  color: #6e6e6e;
}

.result-card-description {
  font-size: 15px;
  margin-top: 20px;
  max-width: 350px;
  flex-grow: 1;
}
</style>

<script setup>
defineProps({
  place: Object,
  idx: Number,
});
</script>

<template>
  <div class="result-card-container">
    <div :class="idx % 2 === 0 ? 'right-box' : 'left-box'">
      <div class="result-card-index">{{ idx }}</div>
    </div>
    <div class="result-card" :class="idx % 2 === 0 ? 'right' : 'left'">
      <img class="result-card-thumbnail" />
      <div class="result-card-detail">
        <div class="result-card-title"></div>
        <div class="result-card-category"></div>
        <div class="result-card-description"></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.result-card-container {
  position: relative;
  width: 100vw;
  height: 250px;
}

.result-card {
  position: absolute;
  top: 70px;
  width: 500px;
  height: 175px;
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
</style>

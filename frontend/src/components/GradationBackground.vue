<script setup></script>

<template>
  <div class="gradation-background"></div>
</template>

<style scoped>
.gradation-background {
  z-index: -1;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  background-color: black;
  background-image: linear-gradient(0deg, black 0%, #ffffff 100%);
  opacity: 0.2;
}
</style>

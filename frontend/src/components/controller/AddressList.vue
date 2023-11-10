<script setup>
defineProps({
  addressList: Array,
});

const emit = defineEmits(["maekInfoWindow"]);

const onClick = (item) => {
  emit("setInfoWindow", item);
};
</script>

<template>
  <ul class="address-list">
    <li class="address-item" v-for="item in addressList" v-key="item.address">{{ item.address }}</li>
  </ul>
</template>

<style scoped>
.address-list {
  box-sizing: border-box;
  width: 300px;
  height: 800px;
  margin-top: 20px;
  border: 1px solid rgba(0, 0, 0, 0.7);
  border-radius: 4px;
  overflow: scroll;
}

.address-item:first-child {
  border-top: 1px solid rgba(0, 0, 0, 0.5);
}
.address-item {
  width: 300px;
  height: 100px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.5);
}
</style>

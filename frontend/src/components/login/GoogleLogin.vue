<script setup>
import { onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "../../util/request";

const route = useRoute();
const router = useRouter();

onMounted(() => {
  const path = "/api/socialLogin";
  const payload = {
    code: route.query.code,
    registrationId: "google",
  };
  axios.post(path, payload).then((response) => {
    if (response.data) {
      localStorage.setItem("access_token", response.data);
      localStorage.setItem("registrationId", "google");
      router.push("/");
    } else {
      router.replace("/login");
    }
  });
});
</script>

<template>
  <div></div>
</template>

<style scoped></style>

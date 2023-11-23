import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
  const userInfo = ref({ userEmail: "ssafy@ssafy.com", userName: "김싸피", birth: "1998-05-15" });
  const accessToken = ref(null);
  const socialType = ref(null);

  return { userInfo, accessToken, socialType };
});

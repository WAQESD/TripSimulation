import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import TripView from "../views/TripView.vue";
import LoginView from "../views/LoginView.vue";
import ResultView from "../views/ResultView.vue";
import SignupView from "../views/SignupView.vue";
import MyPageView from "../views/MyPageView.vue";
import KakaoLogin from "../components/login/KakaoLogin.vue";
import GoogleLogin from "../components/login/GoogleLogin.vue";

import { useModalStore } from "../stores/modal";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/login",
      name: "login",
      component: LoginView,
      children: [
        {
          path: "kakao",
          name: "kakao",
          component: KakaoLogin,
        },
        {
          path: "google",
          name: "google",
          component: GoogleLogin,
        },
      ],
    },
    {
      path: "/signup",
      name: "signup",
      component: SignupView,
    },
    {
      path: "/trip",
      name: "trip",
      component: TripView,
    },
    {
      path: "/mypage",
      name: "mypage",
      component: MyPageView,
    },
    {
      path: "/result",
      name: "result",
      component: ResultView,
    },
  ],
});

router.beforeEach((to, from, next) => {
  const modalStore = useModalStore();
  modalStore.setModal(false);
  if (next) next();
  return to;
});

export default router;

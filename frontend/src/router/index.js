import { createRouter, createWebHistory, useRouter } from "vue-router";
import HomeView from "../views/HomeView.vue";
import TripView from "../views/TripView.vue";
import LoginView from "../views/LoginView.vue";
import ResultView from "../views/ResultView.vue";
import SignupView from "../views/SignupView.vue";
import MyPageView from "../views/MyPageView.vue";
import KakaoLogin from "../components/login/KakaoLogin.vue";
import GoogleLogin from "../components/login/GoogleLogin.vue";

import { useModalStore } from "../stores/modal";
import { usePlayerStore } from "../stores/player";
import { usePathStore } from "../stores/path";
import { useUserStore } from "../stores/user";

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
      beforeEnter: (to, from, next) => {
        usePlayerStore().init();
        if (next) next();
        return to;
      },
    },
    {
      path: "/mypage",
      name: "mypage",
      component: MyPageView,
      beforeEnter: (to, from, next) => {
        const userStore = useUserStore();

        if (!userStore.userInfo) {
          next("/login");
        }
        usePathStore().getPathList(userStore.userInfo);
        next();
      },
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

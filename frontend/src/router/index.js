import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import TripView from "../views/TripView.vue";
import LoginView from "../views/LoginView.vue";
import SignupView from "../views/SignupView.vue";
import KakaoLogin from "../components/login/KakaoLogin.vue";

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
  ],
});

export default router;

import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/tripsimulation",
      name: "home",
      component: HomeView,
    },
    {
      path: "/",
      name: "tripsimulation",
      component: () => import("../views/TripSimulation.vue"),
    },
  ],
});

export default router;

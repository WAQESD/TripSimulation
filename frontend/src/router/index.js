import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import TripSimulation from "../views/TripSimulation.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/trip",
      name: "trip",
      component: TripSimulation,
    },
  ],
});

export default router;

import { defineStore } from "pinia";
import { ref } from "vue";

export const useTimeStore = defineStore("time", () => {
  const startTime = ref(new Date());
  const currentTime = ref(new Date());
  const estimatedGoalTime = ref(new Date());

  const setStartTime = (hours, minutes) => {
    startTime.value = new Date();
    startTime.value.setHours(hours);
    startTime.value.setMinutes(minutes);

    currentTime.value = new Date(startTime.value.getTime());
  };

  const setEstimatedGoalTime = (time) => {
    estimatedGoalTime.value = new Date(time);
  };

  const setCurrentTime = (time) => {
    currentTime.value = new Date();
    currentTime.value.setTime(time);
  };

  const addCurrentTime = (ms) => {
    currentTime.value = new Date(currentTime.value.getTime() + parseInt(ms));
  };

  return {
    startTime,
    currentTime,
    estimatedGoalTime,
    setStartTime,
    setEstimatedGoalTime,
    setCurrentTime,
    addCurrentTime,
  };
});

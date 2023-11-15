import { defineStore } from "pinia";
import { ref } from "vue";
import { douglasPeucker } from "../util/douglasPeucker";
import { useModalStore } from "./modal";
import axios from "axios";

import SimpleTextModal from "../components/modal/SimpleTextModal.vue";

export const usePlayerStore = defineStore("player", () => {
  const speed = ref(5);
  const currentStart = ref(null);
  const currentGoal = ref(null);
  const isEnd = ref(false);
  const tripStart = ref(false);
  const modalStore = useModalStore();
  const carOverlay = ref(null);
  const isPaused = ref(false);
  const miniMapBounds = ref(false);
  const departureTime = ref(null);
  const arrivalTime = ref(null);

  const width = 44;
  const height = 85;

  let CustomOverlay = null;
  let polylinePath = ref(null);
  let path = null;
  let map = null;
  let startTime = 0;
  let currentIndex = 0;
  let expectedEndTime = 0;

  const increaseSpeed = () => {
    if (speed.value <= 1) return;
    pause();
    speed.value -= 1;
    reStart();
  };

  const decreaseSpeed = () => {
    if (speed.value >= import.meta.env.VITE_MAX_SPEED) return;
    pause();
    speed.value += 1;
    reStart();
  };

  const pause = () => {
    if (isPaused.value) return;
    isPaused.value = true;
    carOverlay.value.pause();
  };

  const reStart = () => {
    if (!isPaused.value) return;
    isPaused.value = false;
    let dist = getDist(currentStart.value, currentGoal.value);
    carOverlay.value.moveTo(carOverlay.value.getPosition(), currentGoal.value, dist, currentIndex);
  };

  let getAngle = (s, e) => {
    if (!currentStart.value || !currentGoal.value) return 0;
    let rad = Math.atan2(e.y - s.y, e.x - s.x);
    return 90 - (rad * 180) / Math.PI;
  };

  let getDist = (s, g) => {
    let sx = s.x * 100000;
    let sy = s.y * 100000;
    let gx = g.x * 100000;
    let gy = g.y * 100000;

    return Math.sqrt((sx - gx) * (sx - gx) + (sy - gy) * (sy - gy));
  };

  let setMap = (newMap) => {
    map = newMap;
    CustomOverlay = function (options) {
      this._element = document.createElement("img");
      this._element.id = "car";
      this._element.src = "/src/assets/images/car.png";
      this._element.style.width = width + "px";
      this._element.style.height = height + "px";
      this._element.style.position = "absolute";

      this.setPosition(options.position);
      this.setMap(options.map || null);
    };

    CustomOverlay.prototype = new window.naver.maps.OverlayView();
    CustomOverlay.prototype.constructor = CustomOverlay;

    CustomOverlay.prototype.setPosition = function (position) {
      this._position = position;
      this.draw();
    };

    CustomOverlay.prototype.getPosition = function () {
      return this._position;
    };

    CustomOverlay.prototype.onAdd = function () {
      var overlayLayer = this.getPanes().overlayLayer;
      overlayLayer.appendChild(this._element);
    };

    CustomOverlay.prototype.draw = function () {
      if (!this.getMap()) return;

      let projection = this.getProjection(),
        position = this.getPosition(),
        pixelPosition = projection.fromCoordToOffset(position);

      this._element.style.left = pixelPosition.x - width / 2 + "px";
      this._element.style.top = pixelPosition.y - height / 2 + "px";
    };

    CustomOverlay.prototype.onRemove = function () {
      this._element.parentNode.removeChild(this._element);
      this._element.replaceWith(this._element.cloneNode(true));
    };

    CustomOverlay.prototype.moveTo = function (from, to, dist, index) {
      currentIndex = index;
      expectedEndTime = dist * 1.5 ** (speed.value - 3);

      this._element.style.transform = `rotate(${getAngle(from, to)}deg)`;
      this._element.style.transition = `left ${expectedEndTime}ms linear, top ${expectedEndTime}ms linear`;

      this.setPosition(to);

      // map.panTo(to, { duration: expectedEndTime, easing: "linear" });

      this._element.addEventListener("transitionstart", () => {
        startTime = performance.now();
      });

      this._element.addEventListener(
        "transitionend",
        () => {
          if (polylinePath.value.length <= index + 2) finish();
          else
            setTimeout(() => {
              if (!isPaused.value) next(index + 1);
            });
        },
        { once: true }
      );
    };

    CustomOverlay.prototype.pause = function () {
      const estimatedTime = performance.now() - startTime;
      const rate = Math.min(1, estimatedTime / expectedEndTime);

      const estimatedPosition = {
        x: currentStart.value.x + (currentGoal.value.x - currentStart.value.x) * rate,
        y: currentStart.value.y + (currentGoal.value.y - currentStart.value.y) * rate,
      };

      this._element.style.transition = ``;
      this.setPosition(estimatedPosition);
      currentStart.value = estimatedPosition;
    };
  };

  const startPath = () => {
    isEnd.value = false;

    modalStore.setModal(true, SimpleTextModal, {
      callback: () => {
        next(0);
      },
      text: "지정한 경로로 시뮬레이션을 시작합니다.",
    });
  };

  const finish = () => {
    modalStore.setModal(true, SimpleTextModal, {
      text: "목적지에 도착했습니다.",
      callback: () => {
        isEnd.value = true;
        tripStart.value = false;
        carOverlay.value.setMap(null);
      },
    });
  };

  const next = (index) => {
    currentStart.value = polylinePath.value[index];
    currentGoal.value = polylinePath.value[index + 1];

    let dist = getDist(currentStart.value, currentGoal.value);
    carOverlay.value.moveTo(currentStart.value, currentGoal.value, dist, index);
  };

  let getPath = async (start, goal) => {
    if (!start || !goal) return;

    let { data } = await axios({
      method: "post",
      // url: "http://ec2-54-180-89-8.ap-northeast-2.compute.amazonaws.com:8080/save",
      url: "http://localhost:8080/save",
      data: {
        name: "PATH",
        start: { lng: start.x, lat: start.y },
        goal: { lng: goal.x, lat: goal.y },
      },
    });

    let bbox = data.route.traoptimal[0].summary.bbox;
    miniMapBounds.value = new window.naver.maps.LatLngBounds(
      new window.naver.maps.LatLng(bbox[0][1], bbox[0][0]),
      new window.naver.maps.LatLng(bbox[1][1], bbox[1][0])
    );

    let pathData = data.route.traoptimal[0].path;
    let zipped = douglasPeucker(pathData, 0.00002);

    let [hour, minute, second] = new Date(data.route.traoptimal[0].summary.departureTime)
      .toTimeString()
      .split(" ")[0]
      .split(":");
    departureTime.value = { hour, minute, second };

    [hour, minute, second] = new Date(
      new Date(data.route.traoptimal[0].summary.departureTime).getTime() + data.route.traoptimal[0].summary.duration
    )
      .toTimeString()
      .split(" ")[0]
      .split(":");
    arrivalTime.value = { hour, minute, second };

    console.log("before : ", pathData.length, "after : ", zipped.length);

    polylinePath.value = zipped.filter((p) => !!p).map(([lng, lat]) => new window.naver.maps.LatLng(lat, lng));
    // polylinePath.value = pathData.map(([lng, lat]) => new window.naver.maps.LatLng(lat, lng));

    if (path) path.setMap(null);

    path = new window.naver.maps.Polyline({
      path: polylinePath.value,
      strokeColor: "#5347AA",
      map: map,
    });

    map.setCenter(polylinePath.value[0]);
    map.setZoom(17);

    carOverlay.value = new CustomOverlay({
      position: polylinePath.value[0],
      map: map,
    });

    tripStart.value = true;
    startPath();
  };

  return {
    setMap,
    getPath,
    decreaseSpeed,
    increaseSpeed,
    pause,
    reStart,
    polylinePath,
    miniMapBounds,
    currentStart,
    currentGoal,
    getAngle,
    departureTime,
    arrivalTime,
    tripStart,
  };
});

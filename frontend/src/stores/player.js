import { defineStore } from "pinia";
import { ref, watch } from "vue";
import { douglasPeucker } from "../util/douglasPeucker";
import { useModalStore } from "./modal";
import axios from "axios";

import SimpleTextModal from "../components/modal/SimpleTextModal.vue";

export const usePlayerStore = defineStore("player", () => {
  const speed = ref(1);
  const currentStart = ref(null);
  const currentGoal = ref(null);
  const isEnd = ref(false);
  const modalStore = useModalStore();
  const carOverlay = ref(null);

  const width = 44;
  const height = 85;

  let CustomOverlay = null;
  let polylinePath = null;
  let path = null;
  let map = null;
  let startTime = 0;

  let getAngle = (s, e) => {
    if (!currentStart.value || !currentGoal.value) return 0;
    let rad = Math.atan2(e.y - s.y, e.x - s.x);
    return 90 - (rad * 180) / Math.PI;
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

      var projection = this.getProjection(),
        position = this.getPosition(),
        pixelPosition = projection.fromCoordToOffset(position);

      this._element.style.left = pixelPosition.x - width / 2 + "px";
      this._element.style.top = pixelPosition.y - height / 2 + "px";
    };

    CustomOverlay.prototype.onRemove = function () {
      this._element.parentNode.removeChild(this._element);
      this._element.replaceWith(this._element.cloneNode(true));
    };

    CustomOverlay.prototype.moveTo = function (from, to, time, index) {
      this._element.style.transform = `rotate(${getAngle(from, to)}deg)`;

      this._element.style.transition = `left ${time}ms linear, top ${time}ms linear`;
      this.setPosition(to);
      startTime = performance.now();
      map.panTo(to, { duration: time, easing: "linear" });

      this._element.addEventListener(
        "transitionend",
        () => {
          console.log(time, performance.now() - startTime);
          if (polylinePath.length <= index + 2) finish();
          else
            setTimeout(() => {
              next(index + 1);
            });
        },
        { once: true }
      );
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
        carOverlay.value.setMap(null);
      },
    });
  };

  const next = (index) => {
    currentStart.value = polylinePath[index];
    currentGoal.value = polylinePath[index + 1];

    let sx = currentStart.value.x * 100000;
    let sy = currentStart.value.y * 100000;
    let gx = currentGoal.value.x * 100000;
    let gy = currentGoal.value.y * 100000;

    let dist = Math.sqrt((sx - gx) * (sx - gx) + (sy - gy) * (sy - gy));
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

    let pathData = data.route.traoptimal[0].path;
    let zipped = douglasPeucker(pathData, 0.00002);

    console.log("before : ", pathData.length, "after : ", zipped.length);

    polylinePath = zipped.filter((p) => !!p).map(([lng, lat]) => new window.naver.maps.LatLng(lat, lng));
    // polylinePath = pathData.map(([lng, lat]) => new window.naver.maps.LatLng(lat, lng));

    if (path) path.setMap(null);

    path = new window.naver.maps.Polyline({
      path: polylinePath,
      strokeColor: "#5347AA",
      map: map,
    });

    map.setCenter(polylinePath[0]);
    map.setZoom(17);

    carOverlay.value = new CustomOverlay({
      position: polylinePath[0],
      map: map,
    });

    startPath();
  };

  return { setMap, getPath };
});

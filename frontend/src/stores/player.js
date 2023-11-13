import { defineStore } from "pinia";
import { ref, computed } from "vue";
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

  var CustomOverlay = function (options) {
    this._element = document.createElement("div");
    this._element.innerHTML = options.html;
    this._element.style.position = "absolute";

    this.setPosition(options.position);
    this.setMap(options.map || null);
  };

  let polylinePath = null;
  let path = null;

  let map = null;

  const car = computed(() => {
    if (isEnd.value) return "";
    return `<img id="car" src="/src/assets/images/car.png" style="width: ${width}px;
      height: ${height}px;"/>`;
  });

  let getAngle = (s, e) => {
    if (!currentStart.value || !currentGoal.value) return 0;
    let rad = Math.atan2(e.y - s.y, e.x - s.x);
    return 90 - (rad * 180) / Math.PI;
  };

  let setMap = (newMap) => {
    map = newMap;

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
      if (!this.getMap()) {
        return;
      }

      var projection = this.getProjection(),
        position = this.getPosition(),
        pixelPosition = projection.fromCoordToOffset(position);

      this._element.style.left = pixelPosition.x - width / 2 + "px";
      this._element.style.top = pixelPosition.y - height / 2 + "px";
    };

    CustomOverlay.prototype.onRemove = function () {
      var overlayLayer = this.getPanes().overlayLayer;

      this._element.remove();
      this._element.off();
    };

    CustomOverlay.prototype.moveTo = function (from, to, time, index) {
      var projection = this.getProjection();

      const fromPixel = projection.fromCoordToOffset(from);
      const toPixel = projection.fromCoordToOffset(to);

      this._element.style.transform = `rotate(${getAngle(from, to)}deg)`;

      this._element.style.transition = `left ${time}ms linear, top ${time}ms linear`;
      this._element.style.left = toPixel.x - width / 2 + "px";
      this._element.style.top = toPixel.y - height / 2 + "px";
      // map.setCenter(from);

      this._element.addEventListener(
        "transitionend",
        () => {
          next(index + 1);
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
      },
    });
  };

  const next = (index) => {
    if (polylinePath.length <= index + 1) {
      finish();
      return;
    }

    console.log(index);

    currentStart.value = polylinePath[index];
    currentGoal.value = polylinePath[index + 1];

    let sx = currentStart.value.x * 100000;
    let sy = currentStart.value.y * 100000;
    let gx = currentGoal.value.x * 100000;
    let gy = currentGoal.value.y * 100000;

    let dist = Math.sqrt((sx - gx) * (sx - gx) + (sy - gy) * (sy - gy));

    console.log(dist);
    // carOverlay.value.moveTo(currentStart.value, currentGoal.value, dist * 70000, () => {
    //   next(index + 1);
    // });
    carOverlay.value.moveTo(currentStart.value, currentGoal.value, dist * 50, index);
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

    // polylinePath = zipped.filter((p) => !!p).map(([lng, lat]) => new window.naver.maps.LatLng(lat, lng));
    polylinePath = pathData.map(([lng, lat]) => new window.naver.maps.LatLng(lat, lng));

    if (path) path.setMap(null);

    path = new window.naver.maps.Polyline({
      path: polylinePath,
      strokeColor: "#5347AA",
      map: map,
    });

    map.setCenter(polylinePath[0]);
    map.setZoom(17);

    currentStart.value = polylinePath[0];
    currentGoal.value = polylinePath[1];

    carOverlay.value = new CustomOverlay({
      html: car.value,
      position: polylinePath[0],
      map: map,
    });

    startPath();
  };

  return { setMap, getPath };
});

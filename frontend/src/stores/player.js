import { defineStore } from "pinia";
import { ref } from "vue";
import { douglasPeucker } from "../util/DouglasPeucker";
import { useModalStore } from "./modal";
import { useTimeStore } from "./time";
import { usePlaceStore } from "./place";
import { useRouter } from "vue-router";
import axios from "axios";

import SimpleTextModal from "../components/modal/SimpleTextModal.vue";

export const usePlayerStore = defineStore("player", () => {
  const traceMode = ref(false);
  const tripStart = ref(false);
  const isPaused = ref(false);
  const isEnd = ref(false);
  const map = ref(null);
  const speed = ref(5);

  const currentStart = ref(null);
  const currentGoal = ref(null);

  const router = useRouter();
  const timeStore = useTimeStore();
  const modalStore = useModalStore();
  const placeStore = usePlaceStore();
  const carOverlay = ref(null);

  const miniMapBounds = ref(false);
  const departureTime = ref(null);
  const arrivalTime = ref(null);

  const polylinePath = ref(null);
  const startPlace = ref({ placeId: 0, placeName: "", lat: 0, lng: 0, x: 0, y: 0, address: "", category: "None" });
  const goalPlace = ref({ placeId: 0, placeName: "", lat: 0, lng: 0, x: 0, y: 0, address: "", category: "None" });
  const wayPoints = ref([]);
  const currentWaypointIndex = ref(0);

  const width = 44;
  const height = 85;

  let CustomOverlay = null;
  let path = null;
  let startTime = 0;
  let currentIndex = 0;
  let expectedEndTime = 0;
  let estimatedTime = 0;
  let pathData = null;

  const init = () => {
    startPlace.value = { placeId: 0, placeName: "", lat: 0, lng: 0, x: 0, y: 0, address: "", category: "None" };
    goalPlace.value = { placeId: 0, placeName: "", lat: 0, lng: 0, x: 0, y: 0, address: "", category: "None" };
    wayPoints.value = [];
    currentWaypointIndex.value = 0;

    speed.value = 5;
    tripStart.value = false;
    isPaused.value = false;
    isEnd.value = false;
    miniMapBounds.value = null;
    departureTime.value = null;
    arrivalTime.value = null;
    polylinePath.value = null;
  };

  const increaseSpeed = () => {
    if (speed.value <= 1) return;
    speed.value -= 1;
    if (!isPaused.value) {
      pause();
      reStart();
    }
  };

  const toggleTraceMode = () => {
    if (!traceMode.value) map.value.setCenter(carOverlay.value.getCurrentPosition());
    traceMode.value = !traceMode.value;
  };

  const decreaseSpeed = () => {
    if (speed.value >= import.meta.env.VITE_MAX_SPEED) return;
    speed.value += 1;
    if (!isPaused.value) {
      pause();
      reStart();
    }
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
    if (dist < 0.0000001) next(currentIndex + 1);
    else carOverlay.value.moveTo(carOverlay.value.getPosition(), currentGoal.value, dist, currentIndex);
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
    map.value = newMap;
    CustomOverlay = function (options) {
      this._element = document.createElement("img");
      this._element.id = "car";
      this._element.src = "/src/assets/images/car.png";
      this._element.style.width = width + "px";
      this._element.style.height = height + "px";
      this._element.style.position = "absolute";
      this._element.style["z-index"] = 1;

      this.setPosition(options.position);
      this.setMap(options.map || null);
    };

    window.naver.maps.Event.addListener(map.value, "dragstart", () => {
      map.value.setCenter(map.value.getCenter());
      traceMode.value = false;
    });

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

    CustomOverlay.prototype.getCurrentPosition = function () {
      if (isPaused.value) return currentStart.value;
      else {
        estimatedTime = performance.now() - startTime;
        const rate = Math.min(1, estimatedTime / expectedEndTime);

        return {
          x: currentStart.value.x + (currentGoal.value.x - currentStart.value.x) * rate,
          y: currentStart.value.y + (currentGoal.value.y - currentStart.value.y) * rate,
        };
      }
    };

    CustomOverlay.prototype.moveTo = function (from, to, dist, index) {
      currentIndex = index;
      expectedEndTime = dist * 1.5 ** (speed.value - 3);

      this._element.style.transform = `rotate(${getAngle(from, to)}deg)`;
      this._element.style.transition = `left ${expectedEndTime}ms linear, top ${expectedEndTime}ms linear`;

      this.setPosition(to);

      if (traceMode.value) map.value.panTo(to, { duration: expectedEndTime, easing: "linear" });

      this._element.addEventListener("transitionstart", () => {
        startTime = performance.now();
      });

      this._element.addEventListener(
        "transitionend",
        () => {
          if (polylinePath.value.length <= index + 2) finish();
          else
            setTimeout(() => {
              if (!isPaused.value) {
                next(index + 1);
              }
            });
        },
        { once: true }
      );
    };

    CustomOverlay.prototype.pause = function () {
      estimatedTime = performance.now() - startTime;
      const rate = Math.min(1, estimatedTime / expectedEndTime);

      const estimatedPosition = {
        x: currentStart.value.x + (currentGoal.value.x - currentStart.value.x) * rate,
        y: currentStart.value.y + (currentGoal.value.y - currentStart.value.y) * rate,
      };

      if (rate === 1) {
        estimatedPosition.x = currentGoal.value.x;
        estimatedPosition.y = currentGoal.value.y;
      }

      this._element.style.transition = ``;
      this.setPosition(estimatedPosition);
      currentStart.value = estimatedPosition;
      if (traceMode.value) map.value.setCenter(estimatedPosition);
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
        router.push("/result");
      },
    });
  };

  const next = (index) => {
    currentStart.value = polylinePath.value[index];
    currentGoal.value = polylinePath.value[index + 1];

    placeStore.setCurrentRegion({ x: currentStart.value.x, y: currentStart.value.y });

    if (pathData[index].wayPointIndex > 0) currentWaypointIndex.value = pathData[index].wayPointIndex;

    let dist = getDist(currentStart.value, currentGoal.value);
    carOverlay.value.moveTo(currentStart.value, currentGoal.value, dist, index);
  };

  let startTrip = async () => {
    let start = { x: startPlace.value.x, y: startPlace.value.y };
    let goal = { x: goalPlace.value.x, y: goalPlace.value.y };

    if (!start || !goal) return;

    let { data } = await axios({
      method: "post",
      url: import.meta.env.VITE_DRIVING_BASE_API,
      data: {
        name: "PATH",
        start: { lng: start.x, lat: start.y },
        wayPoints: wayPoints.value.map((wayPoint) => {
          return { lng: wayPoint.x, lat: wayPoint.y };
        }),
        goal: { lng: goal.x, lat: goal.y },
      },
    });

    let bbox = data.route.traoptimal[0].summary.bbox;

    miniMapBounds.value = new window.naver.maps.LatLngBounds(
      new window.naver.maps.LatLng(bbox[0][1], bbox[0][0]),
      new window.naver.maps.LatLng(bbox[1][1], bbox[1][0])
    );

    pathData = data.route.traoptimal[0].path.map((p, i) => {
      return { index: i, wayPointIndex: 0, lng: p[0], lat: p[1], x: p[0], y: p[1] };
    });

    pathData = douglasPeucker(pathData, 0.00002);
    let pathIndex = 0;
    let sumDuration = 0;

    for (
      let i = 0;
      data.route.traoptimal[0].summary.waypoints && i < data.route.traoptimal[0].summary.waypoints.length;
      i++
    ) {
      let waypoint = data.route.traoptimal[0].summary.waypoints[i];

      if (currentWaypointIndex.value + i < wayPoints.value.length) {
        sumDuration += waypoint.duration;
        wayPoints.value[currentWaypointIndex.value + i].arrivalTime = new Date(
          timeStore.startTime.getTime() + sumDuration
        );
      }

      while (pathData[pathIndex].index < waypoint.pointIndex) pathIndex++;
      if (pathData[pathIndex].index === waypoint.pointIndex) pathData[pathIndex].wayPointIndex = i;
      else
        pathData.splice(pathIndex, 0, {
          lng: waypoint.location[0],
          lat: waypoint.location[1],
          x: waypoint.location[0],
          y: waypoint.location[1],
          wayPointIndex: i + 1,
          index: waypoint.pointIndex,
        });
    }

    timeStore.setEstimatedGoalTime(timeStore.startTime.getTime() + data.route.traoptimal[0].summary.duration);
    goalPlace.value.arrivalTime = timeStore.estimatedGoalTime;

    polylinePath.value = pathData.map(({ lng, lat }) => new window.naver.maps.LatLng(lat, lng));

    if (path) path.setMap(null);

    path = new window.naver.maps.Polyline({
      path: polylinePath.value,
      strokeColor: "#5347AA",
      strokeWeight: 4,
      map: map.value,
    });

    map.value.setCenter(polylinePath.value[0]);
    map.value.setZoom(17);

    carOverlay.value = new CustomOverlay({
      position: polylinePath.value[0],
      map: map.value,
    });

    window.naver.maps.Event.addListener(map.value, "zoomstart", () => {
      if (isPaused.value) return;
      pause();
      window.naver.maps.Event.once(map.value, "zoomend", () => {
        setTimeout(reStart, 10);
      });
    });

    window.naver.maps.Event.addListener(map.value, "size_changed", () => {
      if (isPaused.value) return;
      pause();
      setTimeout(reStart, 10);
    });

    tripStart.value = true;
    placeStore.getThumbnailByPlaceName(startPlace.value, (thumbnail) => {
      startPlace.value.thumbnail = thumbnail;
    });

    for (let i = 0; i < wayPoints.value.length; i++) {
      placeStore.getThumbnailByPlaceName(wayPoints.value[i], (thumbnail) => {
        wayPoints.value[i].thumbnail = thumbnail;
      });
    }

    placeStore.getThumbnailByPlaceName(goalPlace.value, (thumbnail) => {
      goalPlace.value.thumbnail = thumbnail;
    });

    startPath();
  };

  const addWaypoint = async (waypoint) => {
    pause();

    wayPoints.value.splice(currentWaypointIndex.value, 0, waypoint);
    placeStore.getThumbnailByPlaceName(wayPoints.value[currentWaypointIndex.value], (thumbnail) => {
      wayPoints.value[currentWaypointIndex.value].thumbnail = thumbnail;
    });

    let { data } = await axios({
      method: "post",
      url: import.meta.env.VITE_DRIVING_BASE_API,
      data: {
        name: "PATH",
        start: { lng: currentStart.value.x, lat: currentStart.value.y },
        wayPoints: [...wayPoints.value].splice(currentWaypointIndex.value).map((wayPoint) => {
          return { lng: wayPoint.x, lat: wayPoint.y };
        }),
        goal: { lng: goalPlace.value.x, lat: goalPlace.value.y },
      },
    });

    let bbox = data.route.traoptimal[0].summary.bbox;
    miniMapBounds.value = new window.naver.maps.LatLngBounds(
      new window.naver.maps.LatLng(
        Math.min(miniMapBounds.value._min._lat, bbox[0][1]),
        Math.min(miniMapBounds.value._min._lng, bbox[0][0])
      ),
      new window.naver.maps.LatLng(
        Math.max(miniMapBounds.value._max._lat, bbox[1][1]),
        Math.max(miniMapBounds.value._max._lng, bbox[1][0])
      )
    );

    let newPathData = data.route.traoptimal[0].path.map((p, i) => {
      return { index: pathData[currentIndex].index + i, wayPointIndex: 0, lng: p[0], lat: p[1], x: p[0], y: p[1] };
    });

    newPathData = douglasPeucker(newPathData, 0.00002);
    let pathIndex = 0;
    let sumDuration = 0;

    for (
      let i = 0;
      data.route.traoptimal[0].summary.waypoints && i < data.route.traoptimal[0].summary.waypoints.length;
      i++
    ) {
      let waypoint = data.route.traoptimal[0].summary.waypoints[i];
      if (currentWaypointIndex.value + i < wayPoints.value.length) {
        sumDuration += waypoint.duration;
        wayPoints.value[currentWaypointIndex.value + i].arrivalTime = new Date(
          timeStore.startTime.getTime() + sumDuration
        );
      }

      while (newPathData[pathIndex].index < waypoint.pointIndex + pathData[currentIndex].index) pathIndex++;
      if (newPathData[pathIndex].index === waypoint.pointIndex + pathData[currentIndex].index)
        newPathData[pathIndex].wayPointIndex = currentWaypointIndex.value + i;
      else
        newPathData.splice(pathIndex, 0, {
          lng: waypoint.location[0],
          lat: waypoint.location[1],
          x: waypoint.location[0],
          y: waypoint.location[1],
          wayPointIndex: i + 1,
          index: pathData[currentIndex].index + waypoint.pointIndex,
        });
    }

    pathData = [...pathData.slice(0, currentIndex + 1), ...newPathData];
    polylinePath.value = pathData.map(({ lng, lat }) => new window.naver.maps.LatLng(lat, lng));

    if (path) path.setMap(null);

    path = new window.naver.maps.Polyline({
      path: polylinePath.value,
      strokeColor: "#5347AA",
      strokeWeight: 4,
      map: map.value,
    });

    currentGoal.value = polylinePath.value[currentIndex + 1];
    reStart();
  };

  const changeGoalPlace = async (place) => {
    goalPlace.value = place;
    if (!tripStart.value) return;
    pause();

    placeStore.getThumbnailByPlaceName(goalPlace.value, (thumbnail) => {
      goalPlace.value.thumbnail = thumbnail;
    });

    let { data } = await axios({
      method: "post",
      url: import.meta.env.VITE_DRIVING_BASE_API,
      data: {
        name: "PATH",
        start: { lng: currentStart.value.x, lat: currentStart.value.y },
        wayPoints: [...wayPoints.value].splice(currentWaypointIndex.value).map((wayPoint) => {
          return { lng: wayPoint.x, lat: wayPoint.y };
        }),
        goal: { lng: goalPlace.value.x, lat: goalPlace.value.y },
      },
    });

    let bbox = data.route.traoptimal[0].summary.bbox;
    miniMapBounds.value = new window.naver.maps.LatLngBounds(
      new window.naver.maps.LatLng(
        Math.min(miniMapBounds.value._min._lat, bbox[0][1]),
        Math.min(miniMapBounds.value._min._lng, bbox[0][0])
      ),
      new window.naver.maps.LatLng(
        Math.max(miniMapBounds.value._max._lat, bbox[1][1]),
        Math.max(miniMapBounds.value._max._lng, bbox[1][0])
      )
    );

    let newPathData = data.route.traoptimal[0].path.map((p, i) => {
      return { index: pathData[currentIndex].index + i, wayPointIndex: 0, lng: p[0], lat: p[1], x: p[0], y: p[1] };
    });

    newPathData = douglasPeucker(newPathData, 0.00002);
    let pathIndex = 0;
    let sumDuration = 0;

    for (
      let i = 0;
      data.route.traoptimal[0].summary.waypoints && i < data.route.traoptimal[0].summary.waypoints.length;
      i++
    ) {
      let waypoint = data.route.traoptimal[0].summary.waypoints[i];
      if (currentWaypointIndex.value + i < wayPoints.value.length) {
        sumDuration += waypoint.duration;
        wayPoints.value[currentWaypointIndex.value + i].arrivalTime = new Date(
          timeStore.startTime.getTime() + sumDuration
        );
      }
      while (
        newPathData[pathIndex] &&
        newPathData[pathIndex].index < waypoint.pointIndex + pathData[currentIndex].index
      )
        pathIndex++;
      if (newPathData[pathIndex].index === waypoint.pointIndex + pathData[currentIndex].index)
        newPathData[pathIndex].wayPointIndex = currentWaypointIndex.value + i + 1;
      else
        newPathData.splice(pathIndex, 0, {
          lng: waypoint.location[0],
          lat: waypoint.location[1],
          x: waypoint.location[0],
          y: waypoint.location[1],
          wayPointIndex: currentWaypointIndex.value + i + 1,
          index: pathData[currentIndex].index + waypoint.pointIndex,
        });
    }
    pathData = [...pathData.slice(0, currentIndex + 1), ...newPathData];
    polylinePath.value = pathData.map(({ lng, lat }) => new window.naver.maps.LatLng(lat, lng));

    if (path) path.setMap(null);

    path = new window.naver.maps.Polyline({
      path: polylinePath.value,
      strokeColor: "#5347AA",
      strokeWeight: 4,
      map: map.value,
    });

    currentGoal.value = polylinePath.value[currentIndex + 1];
    reStart();
  };

  const setStartPlace = (place) => {
    startPlace.value = place;
  };

  const setGoalPlace = (place) => {
    goalPlace.value = place;
  };

  const removeWayPoint = (place) => {
    let idx = -1;
    wayPoints.value.forEach((waypoint, i) => {
      if (place.placeId === waypoint.placeId) idx = i;
    });

    wayPoints.value.splice(idx, 1);
  };

  const pushWaypoint = (waypoint) => {
    wayPoints.value.push(waypoint);
  };

  return {
    startTrip,
    setMap,
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
    map,
    startPlace,
    goalPlace,
    wayPoints,
    toggleTraceMode,
    addWaypoint,
    setStartPlace,
    setGoalPlace,
    removeWayPoint,
    pushWaypoint,
    changeGoalPlace,
    init,
  };
});

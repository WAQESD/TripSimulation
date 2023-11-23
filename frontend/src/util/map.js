import { usePlayerStore } from "../stores/player";
import { rabinKarpHash } from "./rabinKarp";

export const initController = (map, el, position) => {
  window.naver.maps.Event.once(map, "init", function () {
    map.controls[position].push(el);
  });
};

export const addController = (map, el, position) => {
  map.controls[position].push(el);
};

export const removeController = (map, el, position) => {
  map.controls[position].removeElement(el);
};

export function makeAddress(item) {
  if (!item) {
    return;
  }

  var name = item.name,
    region = item.region,
    land = item.land,
    isRoadAddress = name === "roadaddr";

  var sido = "",
    sigugun = "",
    dongmyun = "",
    ri = "",
    rest = "";

  if (hasArea(region.area1)) {
    sido = region.area1.name;
  }

  if (hasArea(region.area2)) {
    sigugun = region.area2.name;
  }

  if (hasArea(region.area3)) {
    dongmyun = region.area3.name;
  }

  if (hasArea(region.area4)) {
    ri = region.area4.name;
  }

  if (land) {
    if (hasData(land.number1)) {
      if (hasData(land.type) && land.type === "2") {
        rest += "산";
      }

      rest += land.number1;

      if (hasData(land.number2)) {
        rest += "-" + land.number2;
      }
    }

    if (isRoadAddress === true) {
      if (checkLastString(dongmyun, "면")) {
        ri = land.name;
      } else {
        dongmyun = land.name;
        ri = "";
      }

      if (hasAddition(land.addition0)) {
        rest += " " + land.addition0.value;
      }
    }
  }

  return [sido, sigugun, dongmyun, ri, rest].join(" ");
}

function hasArea(area) {
  return !!(area && area.name && area.name !== "");
}

function hasData(data) {
  return !!(data && data !== "");
}

function checkLastString(word, lastString) {
  return new RegExp(lastString + "$").test(word);
}

function hasAddition(addition) {
  return !!(addition && addition.value);
}

export const makeInfoWindowByPlace = (place, infoWindow) => {
  const playerStore = usePlayerStore();

  makeInfoWindow(
    infoWindow,
    [
      '<div style="padding: 16px; ">',
      '<h4 style="margin-top:5px; text-align:center">' + place.placeName + "</h4>",
      place.address,
      `
      <div style="text-align: center; margin-top: 12px">
      ` +
        (playerStore.tripStart ? "" : `<button id="info-start-btn">출발</button>`) +
        `<button id="info-waypoint-btn">경유</button>` +
        `<button id="info-goal-btn" >도착</button>
      </div>`,
      "</div>",
    ].join("\n"),
    place
  );
};

export const makeInfoWindowByCoord = (coord, infoWindow) => {
  window.naver.maps.Service.reverseGeocode(
    {
      coords: coord,
      orders: [window.naver.maps.Service.OrderType.ADDR, window.naver.maps.Service.OrderType.ROAD_ADDR].join(","),
    },
    function (status, response) {
      if (status === window.naver.maps.Service.Status.ERROR) {
        return alert("Something Wrong!");
      }

      let items = response.v2.results;
      let address = "";
      let htmlAddresses = [];

      for (let i = 0, ii = items.length, item; i < ii; i++) {
        item = items[i];
        address = makeAddress(item) || "";
        let point = new window.naver.maps.Point(coord.x, coord.y);
        htmlAddresses.push({ address, point });
      }

      const playerStore = usePlayerStore();

      makeInfoWindow(
        infoWindow,
        [
          '<div style="padding: 16px; ">',
          htmlAddresses.map(({ address }) => address).join("<br />"),
          `
            <div style="text-align: center; margin-top: 12px">` +
            (playerStore.tripStart ? "" : `<button id="info-start-btn">출발</button>`) +
            `<button id="info-waypoint-btn">경유</button>` +
            `<button id="info-goal-btn">도착</button>
            </div>`,
          "</div>",
        ].join("\n"),
        makePlaceByAddress(htmlAddresses[0])
      );
    }
  );
};

export function makeInfoWindow(infoWindow, contents, place) {
  const playerStore = usePlayerStore();

  infoWindow.setContent(contents);
  infoWindow.open(playerStore.map, { x: place.x, y: place.y });

  if (!playerStore.tripStart) {
    document.querySelector("#info-start-btn").addEventListener("click", () => {
      playerStore.setStartPlace(place);

      infoWindow.close();
    });
  }

  document.querySelector("#info-waypoint-btn").addEventListener("click", () => {
    if (playerStore.tripStart) playerStore.addWaypoint(place);
    else playerStore.pushWaypoint(place);

    infoWindow.close();
  });

  document.querySelector("#info-goal-btn").addEventListener("click", () => {
    playerStore.changeGoalPlace(place);
    infoWindow.close();
  });
}

export const makePlaceByAddress = (htmlAddress) => {
  return {
    lng: htmlAddress.point.x,
    lat: htmlAddress.point.y,
    x: htmlAddress.point.x,
    y: htmlAddress.point.y,
    placeId: rabinKarpHash(htmlAddress.address),
    address: htmlAddress.address,
    placeName: htmlAddress.address,
  };
};

export const getBoundsByPathList = (path) => {
  const latList = path.map(({ lat }) => lat);
  const lngList = path.map(({ lng }) => lng);

  return new window.naver.maps.LatLngBounds(
    new window.naver.maps.LatLng(Math.min(...latList), Math.min(...lngList)),
    new window.naver.maps.LatLng(Math.max(...latList), Math.max(...lngList))
  );
};

export const getStartIcon = () => {
  return `
    <div class="start-place-marker">
      <img class="start-place-marker-icon" src="./src/assets/images/start_marker.png" width="36", height="36">
    </div>
  `;
};

export const getGoalIcon = () => {
  return `
    <div class="goal-place-marker">
      <img class="goal-place-marker-icon"  src="./src/assets/images/goal_marker.png" width="36", height="36">
    </div>
  `;
};

export const getWayPointIcon = (idx) => {
  return `
    <div class="waypoint-place-marker">
      <img class="waypoint-place-marker-icon" src="./src/assets/images/waypoint_marker.png" width="36", height="36">
      <div class="waypoint-place-marker-text">${idx}</div>
      </img>
    </div>
  `;
};

export const getPlaceMarker = (placeName) => {
  return `
    <div class="place-marker">
      <div class="place-marker-name">${placeName}</div>
    </div>
  `;
};

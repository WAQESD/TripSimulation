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

export const searchAddressToCoordinate = (
  address,
  map,
  infoWindow,
  startPos,
  goalPos,
  startAddr,
  goalAddr,
  searchResults
) => {
  if (!address) {
    searchResults.value = [];
    return;
  }
  window.naver.maps.Service.geocode(
    {
      query: address,
    },
    function (status, response) {
      if (status === window.naver.maps.Service.Status.ERROR) {
        return;
      }

      if (response.v2.meta.totalCount === 0) {
        searchResults.value = [];
        return;
      }

      var htmlAddresses = [];
      let point = {};
      for (let item of response.v2.addresses) {
        point = new window.naver.maps.Point(item.x, item.y);
        htmlAddresses.push({ address: item.roadAddress, point });
      }

      infoWindow.setContent(
        [
          '<div style="padding: 16px; ">',
          '<h4 style="margin-top:5px; text-align:center">검색 주소 : ' + address + "</h4>",
          htmlAddresses[0].address,
          `
          <div style="text-align: center; margin-top: 12px">
            <button id="info-start-btn" style="width:60px; height:30px; border-radius:15px; font-family: 'Pretendard-Regular'; cursor:pointer;" >출발</button>
            <button id="info-goal-btn" style="width:60px; height:30px; border-radius:15px; font-family: 'Pretendard-Regular'; cursor:pointer;" >도착</button>
          </div>
          `,
          "</div>",
        ].join("\n")
      );

      map.setCenter(htmlAddresses[0].point);
      infoWindow.open(map, htmlAddresses[0].point);

      document.querySelector("#info-start-btn").addEventListener("click", () => {
        startPos.value = htmlAddresses[0].point;
        startAddr.value = htmlAddresses[0].address;
        infoWindow.close();
      });

      document.querySelector("#info-goal-btn").addEventListener("click", () => {
        goalPos.value = htmlAddresses[0].point;
        goalAddr.value = htmlAddresses[0].address;
        infoWindow.close();
      });

      searchResults.value = htmlAddresses;
    }
  );
};

export const makeInfoWindowByCoord = (coord, infoWindow, map, startPos, startAddr, goalPos, goalAddr) => {
  window.naver.maps.Service.reverseGeocode(
    {
      coords: coord,
      orders: [window.naver.maps.Service.OrderType.ADDR, window.naver.maps.Service.OrderType.ROAD_ADDR].join(","),
    },
    function (status, response) {
      if (status === window.naver.maps.Service.Status.ERROR) {
        return alert("Something Wrong!");
      }

      var items = response.v2.results,
        address = "",
        htmlAddresses = [];

      for (var i = 0, ii = items.length, item; i < ii; i++) {
        item = items[i];
        address = makeAddress(item) || "";

        htmlAddresses.push(address);
      }

      infoWindow.setContent(
        [
          '<div style="padding: 16px; ">',
          htmlAddresses.join("<br />"),
          `
            <div style="text-align: center; margin-top: 12px">
              <button id="info-start-btn" style="width:60px; height:30px; border-radius:15px; font-family: 'Pretendard-Regular'; cursor:pointer;" >출발</button>
              <button id="info-goal-btn" style="width:60px; height:30px; border-radius:15px; font-family: 'Pretendard-Regular'; cursor:pointer;" >도착</button>
            </div>
          `,
          "</div>",
        ].join("\n")
      );

      infoWindow.open(map, coord);

      document.querySelector("#info-start-btn").addEventListener("click", () => {
        startPos.value = coord;
        startAddr.value = htmlAddresses[0];
        infoWindow.close();
      });

      document.querySelector("#info-goal-btn").addEventListener("click", () => {
        goalPos.value = coord;
        goalAddr.value = htmlAddresses[0];
        infoWindow.close();
      });
    }
  );
};

// export var CustomOverlay = function (options) {
//   this._element = document.createElement("div");
//   this._element.innerHTML = options.html;

//   this.setPosition(options.position);
//   this.setMap(options.map || null);
// };

// CustomOverlay.prototype = new window.naver.maps.OverlayView();
// CustomOverlay.prototype.constructor = CustomOverlay;

// CustomOverlay.prototype.setPosition = function (position) {
//   this._position = position;
//   this.draw();
// };

// CustomOverlay.prototype.getPosition = function () {
//   return this._position;
// };

// CustomOverlay.prototype.onAdd = function () {
//   var overlayLayer = this.getPanes().overlayLayer;

//   this._element.appendTo(overlayLayer);
// };

// CustomOverlay.prototype.draw = function () {
//   if (!this.getMap()) {
//     return;
//   }

//   var projection = this.getProjection(),
//     position = this.getPosition(),
//     pixelPosition = projection.fromCoordToOffset(position);

//   this._element.css("left", pixelPosition.x);
//   this._element.css("top", pixelPosition.y);
// };

// CustomOverlay.prototype.onRemove = function () {
//   var overlayLayer = this.getPanes().overlayLayer;

//   this._element.remove();
//   this._element.off();
// };

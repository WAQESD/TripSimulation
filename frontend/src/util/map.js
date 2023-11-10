export const addController = (map, el, position) => {
  window.naver.maps.Event.once(map, "init", function () {
    map.controls[position].push(el);
  });
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

export const searchAddressToCoordinate = (address, map, infoWindow, startPos, goalPos, startAddr, goalAddr) => {
  window.naver.maps.Service.geocode(
    {
      query: address,
    },
    function (status, response) {
      if (status === window.naver.maps.Service.Status.ERROR) {
        return;
      }

      if (response.v2.meta.totalCount === 0) {
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

      return htmlAddresses;
    }
  );
};

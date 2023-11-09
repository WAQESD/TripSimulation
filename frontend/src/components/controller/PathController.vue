<script setup>
import { ref, computed, onMounted, nextTick } from "vue";
import { addController, makeAddress, searchAddressToCoordinate } from "../../util/map";

const props = defineProps({
  map: Object,
});
const emit = defineEmits(["getPath"]);

const startPos = ref(null);
const goalPos = ref(null);

const startAddr = ref("");
const goalAddr = ref("");

const isClosed = ref(false);
const controllerEl = ref(null);

const btnIcon = computed(() => (isClosed.value ? ">" : "<"));

const toggleController = () => {
  isClosed.value = !isClosed.value;
};

let infoWindow = new window.naver.maps.InfoWindow({
  anchorSkew: true,
});

const searchAddr = (e) => {
  searchAddressToCoordinate(e.target.value, props.map, infoWindow, startPos, goalPos, startAddr, goalAddr);
};

onMounted(() => {
  nextTick(() => {
    addController(props.map, controllerEl.value, window.naver.maps.Position.TOP_LEFT);

    window.naver.maps.Event.addListener(props.map, "click", function ({ coord }) {
      if (infoWindow) infoWindow.close();

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

          infoWindow.open(props.map, coord);

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
    });
  });
});

const getPath = computed(() => () => {
  emit("getPath", startPos.value, goalPos.value);
});
</script>

<template>
  <div :class="isClosed ? 'closed' : ''" id="controller" ref="controllerEl">
    <div class="controller-input-container">
      <div class="input-wrapper">
        <label id="label-start" for="start">출 발</label>
        <input
          type="text"
          placeholder="출발지 입력"
          id="start"
          v-model="startAddr"
          @blur="
            (e) => {
              searchAddr(e);
            }
          "
        />
      </div>
      <div class="input-wrapper">
        <label id="label-goal" for="goal">도 착</label>
        <input
          type="text"
          placeholder="도착지 입력"
          id="goal"
          v-model="goalAddr"
          @blur="
            (e) => {
              searchAddr(e);
            }
          "
        />
      </div>
      <button type="button" @click="getPath">경로 찾기</button>
    </div>
  </div>
  <div class="close-btn" :class="isClosed ? 'closed' : ''" @click="toggleController">{{ btnIcon }}</div>
</template>

<style scoped>
#controller {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: white;
  height: 40px;
  padding: 40px 0;
  border-radius: 0 16px 16px 0;
  border-right: 1px solid rgba(0, 0, 0, 0.3);
  box-sizing: border-box;
  width: 400px;
  height: 100vh;
  transition: transform 0.5s;
}

.controller-input-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

#controller button {
  background-color: white;
  border: 1px solid rgba(0, 0, 0, 0.5);
  border-radius: 4px;
  cursor: pointer;
  padding: 2px 8px;
  margin-top: 20px;
  height: 40px;
  width: 330px;
  border-radius: 8px;
  box-sizing: border-box;
  font-family: "Pretendard-Regular";
  font-size: 16px;
}

#controller button:hover {
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
}

label,
input {
  font-family: "Pretendard-Regular";
  display: inline-block;
  box-sizing: border-box;
  height: 40px;
  padding: 0 12px;
  border: 1px solid rgba(0, 0, 0, 0.7);
  line-height: 40px;
  font-size: 16px;
}

label {
  width: 80px;
  text-align: center;
  background-color: #edede9;
}

input {
  width: 250px;
}

.input-wrapper {
  display: flex;
  height: 40px;
}

#label-start {
  border-radius: 8px 0 0 0;
  border-bottom: none;
}
#start {
  border-radius: 0 8px 0 0;
  border-bottom: none;
  border-left: none;
}
#goal {
  border-radius: 0 0 8px 0;
  border-left: none;
}
#label-goal {
  border-radius: 0 0 0 8px;
}

.close-btn {
  position: absolute;
  top: calc(50vh - 40px);
  left: 400px;
  width: 24px;
  height: 80px;
  border: 1px solid rgba(0, 0, 0, 0.3);
  line-height: 80px;
  border-left: none;
  background-color: white;
  border-radius: 0 8px 8px 0;
  cursor: pointer;
  text-align: center;
  transition: transform 0.5s;
}

.closed {
  transform: translateX(-400px);
}
</style>

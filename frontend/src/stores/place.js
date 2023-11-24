import { defineStore } from "pinia";
import { ref, watch } from "vue";
import axios from "axios";

import { useModalStore } from "./modal";
import { usePlayerStore } from "./player";

import RecommendPlaceModal from "../components/modal/RecommendPlaceModal.vue";
import SimpleTextModal from "../components/modal/SimpleTextModal.vue";

export const usePlaceStore = defineStore("place", () => {
  const placeList = ref([]);
  const map = ref(null);
  const currentRegion = ref({ sido: "", gu: "", dong: "" });
  const currentPosition = ref(null);
  const modalStore = useModalStore();
  const playerStore = usePlayerStore();
  const visited = ref(new Set());

  const setPlaceList = (newPlaceList) => {
    placeList.value = newPlaceList;
  };

  const setMap = (newMap) => {
    map.value = newMap;
  };

  const makePlaceObject = (place) => {
    return {
      x: parseFloat(place.x),
      y: parseFloat(place.y),
      lng: parseFloat(place.x),
      lat: parseFloat(place.y),
      address: place.road_address_name || place.address_name,
      placeName: place.place_name,
      category: place.category_name,
      placeId: parseInt(Math.random() * 1e9),
    };
  };

  const getPlaceListByKeword = async (keyword) => {
    let isEnd = false;

    placeList.value = [];

    for (let pageNo = 1; pageNo < 10 && !isEnd; pageNo += 1) {
      const { data } = await axios.get(import.meta.env.VITE_KAKAO_SEARCH_API, {
        params: {
          query: keyword,
          // x: map.value.center.x,
          // y: map.value.center.y,
          page: pageNo,
          // sort: "distance",
        },
        headers: {
          Authorization: `KakaoAK ${import.meta.env.VITE_KAKAO_REST_API_KEY}`,
        },
      });

      isEnd = data.meta.is_end;
      placeList.value = [...placeList.value, ...data.documents.map(makePlaceObject)];
    }
  };

  const getPlaceListCurrentRegion = async () => {
    const codes = ["FD6", "CE7", "CT1", "AT4"];
    placeList.value = [];

    codes.forEach(async (code) => {
      const { data } = await axios.get(import.meta.env.VITE_KAKAO_SEARCH_API, {
        params: {
          query: `${currentRegion.value.sido} ${currentRegion.value.gu} ${currentRegion.value.dong}`,
          x: currentPosition.value.x,
          y: currentPosition.value.y,
          page: 1,
          radius: 3000,
          category_group_code: code,
        },
        headers: {
          Authorization: `KakaoAK ${import.meta.env.VITE_KAKAO_REST_API_KEY}`,
        },
      });
      placeList.value = [...placeList.value, ...data.documents.map(makePlaceObject)];
    });
  };

  const recommendPlaceListBySidoGu = async () => {
    const { data } = await axios.get(import.meta.env.VITE_KAKAO_SEARCH_API, {
      params: {
        query: `${currentRegion.value.sido} ${currentRegion.value.gu}`,
        x: currentPosition.value.x,
        y: currentPosition.value.y,
        page: 1,
        radius: 3000,
        category_group_code: "AT4",
      },
      headers: {
        Authorization: `KakaoAK ${import.meta.env.VITE_KAKAO_REST_API_KEY}`,
      },
    });

    let placeData = data.documents.map(makePlaceObject);
    placeData = placeData.splice(0, 4);
    let cnt = 4;

    for (const place of placeData) {
      getThumbnailByPlaceName(place, (thumbnail) => {
        place.thumbnail = thumbnail;
        cnt -= 1;
        if (cnt == 0) {
          if (playerStore.isEnd) return;
          playerStore.pause();
          modalStore.setModal(true, SimpleTextModal, { text: "새 추천 선택지가 열렸습니다!", callback: recommend });
        }
      });
    }

    const recommend = () => {
      modalStore.setModal(true, RecommendPlaceModal, {
        data: placeData,
        callback: () => {
          playerStore.reStart();
        },
      });
    };
  };

  const setCurrentRegion = async ({ x, y }) => {
    const { data } = await axios.get(import.meta.env.VITE_KAKAO_REGION_API, {
      params: {
        x,
        y,
      },
      headers: { Authorization: `KakaoAK ${import.meta.env.VITE_KAKAO_REST_API_KEY}` },
    });

    if (!data.documents[0]) return null;

    currentPosition.value = { x, y };

    currentRegion.value = {
      sido: data.documents[0].region_1depth_name,
      gu: data.documents[0].region_2depth_name,
      dong: data.documents[0].region_3depth_name,
    };
  };

  const getThumbnailByPlaceName = (place, callback) => {
    if (place.placeName === place.address) return;
    const addr = place.address.split(" ");
    const keyword = addr[0] + " " + addr[1] + " " + place.placeName;

    axios
      .get(import.meta.env.VITE_THUMBNAIL_API + encodeURIComponent(keyword))
      .then(({ data }) => {
        callback(data.thumbnail);
      })
      .catch((e) => {
        console.error(e);
      });
  };

  watch(
    () => currentRegion.value.dong,
    () => {
      getPlaceListCurrentRegion();
    }
  );

  watch(
    () => currentRegion.value.gu,
    (after, before) => {
      if (!before.trim() || visited.value.has(after)) return;
      visited.value.add(after);
      recommendPlaceListBySidoGu();
    }
  );

  return {
    placeList,
    map,
    setPlaceList,
    setMap,
    getPlaceListCurrentRegion,
    setCurrentRegion,
    getPlaceListByKeword,
    getThumbnailByPlaceName,
    recommendPlaceListBySidoGu,
  };
});

import { defineStore } from "pinia";
import { ref, watch } from "vue";
import axios from "axios";

export const usePlaceStore = defineStore("place", () => {
  const placeList = ref([]);
  const map = ref(null);
  const currentRegion = ref({ sido: "", gu: "", dong: "" });
  const currentPosition = ref(null);

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
    const { data } = await axios.get(import.meta.env.VITE_KAKAO_SEARCH_API, {
      params: {
        query: `${currentRegion.value.sido} ${currentRegion.value.gu} ${currentRegion.value.dong}`,
        x: currentPosition.value.x,
        y: currentPosition.value.y,
        page: 1,
        radius: 3000,
      },
      headers: {
        Authorization: `KakaoAK ${import.meta.env.VITE_KAKAO_REST_API_KEY}`,
      },
    });

    placeList.value = data.documents.map(makePlaceObject);
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

  watch(
    () => currentRegion.value.dong,
    () => {
      getPlaceListCurrentRegion();
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
  };
});

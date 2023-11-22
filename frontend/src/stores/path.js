import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../util/request";

export const usePathStore = defineStore("path", () => {
  const pathList = ref([]);
  const path = ref(null);

  const getPathList = async (userInfo) => {
    const { data } = await axios.get("/path/list", { params: userInfo.email });
    pathList.value = data;

    return data;
  };

  const getPathDetail = async (pathId) => {
    const { data } = await axios.get("/path", { params: pathId });
    path.value = data;

    return data;
  };

  const uploadPath = async (path) => {
    const response = await axios.post("/path", path);

    /*
		waypoints: [{placeName: String, lat : Number, lng : Number, arrivalTime : String}],
    pathContent: {
			path: [{lat : Number, lng : Number}],
			pathKey: String,
			regDate: String,
			pathName: String,
			userEmail: String
		}
		*/

    return response;
  };

  return { pathList, getPathList, getPathDetail, uploadPath };
});

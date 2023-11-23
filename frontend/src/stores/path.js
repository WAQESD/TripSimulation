import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../util/request";

export const usePathStore = defineStore("path", () => {
  const pathList = ref([]);
  const path = ref(null);

  const getPathList = async (userInfo) => {
    const result = await axios.get("/path/list", { params: { userEmail: userInfo.userEmail } });
    pathList.value = result.data.map((path) => {
      return { ...path, regDate: new Date(path.regDate) };
    });
  };

  const getPathDetail = async (pathId) => {
    const { data } = await axios.get("/path", { params: pathId });
    path.value = data;

    return data;
  };

  const uploadPath = async (path) => {
    const response = await axios.post("/path", path);

    /*{
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

  const timeToString = (date) => {
    return new Date(date).toTimeString().split(" ")[0].slice(0, 5);
  };

  return { pathList, getPathList, getPathDetail, uploadPath, timeToString };
});

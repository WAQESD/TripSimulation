const express = require("express"); // Express 실행
const fs = require("fs");
const { v4 } = require("uuid");
const cors = require("cors");
const bodyParser = require("body-parser");
const axios = require("axios");

const app = express();

app.use(cors());
app.use(bodyParser.json());

app.set("port", process.env.PORT || 8080);
const getUrl = (start, goal) =>
  `https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?start=${start.lng + "," + start.lat}&goal=${
    goal.lng + "," + goal.lat
  }`;

app.listen(app.get("port"), () => {
  console.log(app.get("port"), "번 포트에서 대기 중");
});

app.post("/save", async (req, res) => {
  const { data } = await axios({
    method: "get",
    url: getUrl(req.body.start, req.body.goal),
    headers: {
      "X-NCP-APIGW-API-KEY-ID": "cqmvh7feg7",
      "X-NCP-APIGW-API-KEY": "0wLHLB5BybbEwcs8A2CaU8ISHASlWVHMMpZhKPku",
    },
  });

  fs.writeFileSync("data/" + req.body.name + "-" + v4() + ".json", JSON.stringify(data));

  res.send(data);
});

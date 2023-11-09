export const addController = (map, el, position) => {
  window.naver.maps.Event.once(map, "init", function () {
    map.controls[position].push(el);
  });
};

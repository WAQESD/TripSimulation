export function douglasPeucker(points, epsilon) {
  // 최대 거리를 찾는 함수
  function findPerpendicularDistance(point, lineStart, lineEnd) {
    var dx = lineEnd[0] - lineStart[0];
    var dy = lineEnd[1] - lineStart[1];

    // 선분이 실제로는 점이라면
    if (dx == 0 && dy == 0) {
      dx = point[0] - lineStart[0];
      dy = point[1] - lineStart[1];
      return Math.sqrt(dx * dx + dy * dy);
    }

    var t = ((point[0] - lineStart[0]) * dx + (point[1] - lineStart[1]) * dy) / (dx * dx + dy * dy);

    t = Math.max(0, Math.min(1, t));

    var closestPoint = [lineStart[0] + t * dx, lineStart[1] + t * dy];

    dx = point[0] - closestPoint[0];
    dy = point[1] - closestPoint[1];

    return Math.sqrt(dx * dx + dy * dy);
  }

  // 알고리즘의 메인 함수
  function douglasPeuckerStep(points, firstIndex, lastIndex, epsilon, result) {
    var maxDistance = 0;
    var index = 0;

    for (var i = firstIndex + 1; i < lastIndex; i++) {
      var distance = findPerpendicularDistance(points[i], points[firstIndex], points[lastIndex]);

      if (distance > maxDistance) {
        index = i;
        maxDistance = distance;
      }
    }

    if (maxDistance > epsilon) {
      var recursiveResults1 = douglasPeuckerStep(points, firstIndex, index, epsilon, []);
      var recursiveResults2 = douglasPeuckerStep(points, index, lastIndex, epsilon, []);

      // 병합 결과
      result = recursiveResults1.slice(0, recursiveResults1.length - 1).concat(recursiveResults2);
    } else {
      result = [points[firstIndex], points[lastIndex]];
    }

    return result;
  }

  return douglasPeuckerStep(points, 0, points.length - 1, epsilon, []);
}

import {axiosApi} from "@/api/api";

export default {
    getById(id) {
        return axiosApi.get(
            "/tasks/" + id
        )
    },
    getAll(offset, title, level) {
        const queryParams = {};
        if (offset)
            queryParams.page = offset;
        if (title)
            queryParams.search = title;
        if (level)
            queryParams.level = level;

        return axiosApi.get("/tasks/list", {
            params: queryParams
        })
    },
    changeTaskStatus(taskId, newEnabled) {
      return axiosApi.post(
        "/tasks/" + taskId + "/enable",
          null, {
            params: {
              enable: newEnabled
            }
          }
      )
    }
}
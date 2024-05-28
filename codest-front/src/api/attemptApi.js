import {axiosApi} from "@/api/api";

export default {

    sendAttempt(data) {
        return axiosApi.post(
            "/v1/solutions/task/" + data.taskId,
            data
        )
    },
    getAttemptsByTaskId(taskId) {
        return axiosApi.get(
            "/v1/tasks/" + taskId + "/solutions",
        )
    },
    getAttemptById(id) {
        return axiosApi.get(
            "/v1/solutions/" + id,
        )
    }
}
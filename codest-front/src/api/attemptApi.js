import {axiosApi} from "@/api/api";

export default {

    sendAttempt(data) {
        return axiosApi.post(
            "/solutions/task/" + data.taskId,
            data
        )
    },
    getAttemptsByTaskId(taskId) {
        return axiosApi.get(
            "/tasks/" + taskId + "/solutions",
        )
    },
    getAttemptById(id) {
        return axiosApi.get(
            "/solutions/" + id,
        )
    }
}
import {EmployeeApi, DepartmentApi, MeetingApi} from "./RestApi";

const baseurl = "http://127.0.0.1:8080";

export const EmpApi = new EmployeeApi(baseurl);
export const DepApi = new DepartmentApi(baseurl);
export const MetApi = new MeetingApi(baseurl);

import {Item} from "./item";

export interface User {
  email: string;
  contact: string;
  locationId: string;
  student: boolean;
  suburb?: string;
  campusName?: string;
  schoolName?: string;
  townName?: string;
  items?: Item[];
}

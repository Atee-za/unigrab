import {Image} from "./image";

export interface Item {
   id: string;
   name: string;
   type: string;
   price: number;
   description: string;
   town: string;
   suburb: string;
   images: Image[];
 }

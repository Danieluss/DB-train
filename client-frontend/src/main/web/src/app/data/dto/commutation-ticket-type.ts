import {Zone} from "./zone";

export interface PreCommutationTicketType {
  name: string;
  price: number;
  zone: number;
}

export interface CommutationTicketType {
  name: string;
  price: number;
  zone: Zone;
}

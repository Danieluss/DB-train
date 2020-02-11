import { Component, OnInit } from '@angular/core';
import {PathTicket} from "../data/dto/path-ticket";
import {DataService} from "../data/data.service";

@Component({
  selector: 'app-tickets',
  templateUrl: './tickets.component.html',
  styleUrls: ['./tickets.component.scss']
})
export class TicketsComponent implements OnInit {

  pathTickets: PathTicket[];
  displayedColumns: string[] = ['uuid'];

  constructor(private dataService: DataService) { }

  ngOnInit() {
    this.refreshTickets(0, 16);
  }

  refreshTickets(page: number, size: number) {
    this.dataService.getPathTickets(page, size).then((pathTickets) => {
      this.pathTickets = pathTickets;
    })
  }

}

import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {TicketsComponent} from "./tickets/tickets.component";


const routes: Routes = [
  {path: 'tickets', component: TicketsComponent},
  {path: 'buy', component: TicketsComponent},
  {path: 'connection', component: TicketsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

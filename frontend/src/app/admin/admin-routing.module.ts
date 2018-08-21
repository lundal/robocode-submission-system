import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { Route } from '@app/core';
import { AdminComponent } from './admin.component';

const routes: Routes = Route.withShell([
  { path: 'admin/:location', component: AdminComponent, data: {title: 'Admin'} }
]);

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class AdminRoutingModule { }

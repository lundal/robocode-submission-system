import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { Route, extract } from '@app/core';
import { ReglerComponent } from './regler.component';

const routes: Routes = Route.withShell([
  { path: 'regler', component: ReglerComponent, data: { title: 'Regler' } }
]);

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class ReglerRoutingModule { }

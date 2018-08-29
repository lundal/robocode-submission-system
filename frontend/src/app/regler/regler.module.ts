import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { ReglerRoutingModule } from './regler-routing.module';
import { ReglerComponent } from './regler.component';

@NgModule({
  imports: [
    CommonModule,
    TranslateModule,
    ReglerRoutingModule
  ],
  declarations: [
    ReglerComponent
  ]
})
export class ReglerModule { }

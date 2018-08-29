import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { MuligheterRoutingModule } from './muligheter-routing.module';
import { MuligheterComponent } from './muligheter.component';

@NgModule({
  imports: [
    CommonModule,
    TranslateModule,
    MuligheterRoutingModule
  ],
  declarations: [
    MuligheterComponent
  ]
})
export class MuligheterModule { }

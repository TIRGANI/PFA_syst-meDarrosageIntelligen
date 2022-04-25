<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="plantage">
        <h2 class="jh-entity-heading" data-cy="plantageDetailsHeading">
          <span v-text="$t('pfaApp.plantage.detail.title')">Plantage</span> {{ plantage.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('pfaApp.plantage.date')">Date</span>
          </dt>
          <dd>
            <span>{{ plantage.date }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.plantage.nbrPlate')">Nbr Plate</span>
          </dt>
          <dd>
            <span>{{ plantage.nbrPlate }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.plantage.plante')">Plante</span>
          </dt>
          <dd>
            <span v-for="(plante, i) in plantage.plantes" :key="plante.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'PlanteView', params: { planteId: plante.id } }">{{ plante.id }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="plantage.id" :to="{ name: 'PlantageEdit', params: { plantageId: plantage.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./plantage-details.component.ts"></script>

<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="plante">
        <h2 class="jh-entity-heading" data-cy="planteDetailsHeading">
          <span v-text="$t('pfaApp.plante.detail.title')">Plante</span> {{ plante.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('pfaApp.plante.lebelle')">Lebelle</span>
          </dt>
          <dd>
            <span>{{ plante.lebelle }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.plante.photo')">Photo</span>
          </dt>
          <dd>
            <span>{{ plante.photo }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.plante.racin')">Racin</span>
          </dt>
          <dd>
            <span>{{ plante.racin }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.plante.typePlant')">Type Plant</span>
          </dt>
          <dd>
            <div v-if="plante.typePlant">
              <router-link :to="{ name: 'TypePlantView', params: { typePlantId: plante.typePlant.id } }">{{
                plante.typePlant.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="plante.id" :to="{ name: 'PlanteEdit', params: { planteId: plante.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./plante-details.component.ts"></script>

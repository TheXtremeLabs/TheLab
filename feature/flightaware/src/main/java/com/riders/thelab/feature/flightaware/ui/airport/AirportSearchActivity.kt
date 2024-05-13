package com.riders.thelab.feature.flightaware.ui.airport

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


/*
*
* API de données de vol avec des données sur le statut et le suivi des vols à la demande.

FlightAware Firehose
Flux de données de vol en continu pour les intégrations d'entreprise avec des données de vol en temps réel, historiques et prédictives.

FlightAware Foresight
Technologie prédictive pour renforcer la confiance des clients dans vos opérations

Rapid Reports
Achetez rapidement des rapports historiques livrés par courriel.

Custom Reports
Rapports consultatifs détaillés et personnalisés sur les données de suivi des vols.

Integrated Mapping Solutions
Incorporate FlightAware maps in your web and mobile applications
Applications


* */

@AndroidEntryPoint
class AirportSearchActivity : BaseComponentActivity() {

    private val mViewModel: AirportSearchViewModel by viewModels<AirportSearchViewModel>()

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    val airports by mViewModel.airportStateFlow.collectAsStateWithLifecycle()

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AirportSearchContent(
                                airportQuery = mViewModel.airportQuery,
                                onUpdateAirportQuery = mViewModel::updateAirportQuery,
                                isQueryLoading = mViewModel.isQueryLoading,
                                airportList = airports
                            )
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    fun launchAirportDetail(airportID: String) =
        Intent(this, AirportSearchDetailActivity::class.java)
            .apply { this.putExtra(AirportSearchDetailActivity.EXTRA_AIRPORT_ID, airportID) }
            .run { startActivity(this) }
}
//
//  LoginPage.swift
//  iosApp
//
//  Created by Auriane POIRIER on 13/12/2024.
//

import SwiftUI
import shared

struct LoginPage: View {
    @StateObject private var viewModel = LoginViewModel()
    @State private var email: String = ""
    @State private var navigateTo: String? = nil

    var body: some View {
        NavigationStack {
            VStack(spacing: 16) {
                TextField("Email", text: $email)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding()

                Button("Se connecter") {
                    print("LoginPage: Bouton Se connecter cliqué avec email = \(email)")
                    viewModel.login(email: email)
                }
                .buttonStyle(.borderedProminent)

                Text(viewModel.loginMessage)
                    .foregroundColor(viewModel.loginMessage == "Connexion réussie !" ? .green : .red)
                    .padding()
            }
            .navigationTitle("Connexion")
            .onChange(of: viewModel.userRole) {
                print("LoginPage: Détection du rôle utilisateur = \(viewModel.userRole)")
                navigateTo = viewModel.userRole
            }
            .navigationDestination(isPresented: Binding(
                get: { navigateTo != nil },
                set: { if !$0 { navigateTo = nil } }
            )) {
                if navigateTo == "Admin" {
                    AdminHomePage()
                } else {
                    UserHomePage()
                }
            }
        }
    }
}

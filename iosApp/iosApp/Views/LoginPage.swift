//
//  LoginPage.swift
//  iosApp
//
//  Created by Auriane POIRIER on 13/12/2024.
//

import SwiftUI

struct LoginPage: View {
    @StateObject private var viewModel: LoginViewModel

    @State private var email: String = ""
    @State private var password: String = ""
    @State private var navigationTarget: String?

    init(viewModelFactory: LoginViewModelFactory) {
        _viewModel = StateObject(wrappedValue: viewModelFactory.create())
    }

    var body: some View {
        NavigationStack {
            VStack(spacing: 16) {
                TextField("Email", text: $email)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding()

                SecureField("Mot de passe", text: $password)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding()

                Button("Se connecter") {
                    viewModel.login(email: email, password: password)
                    if viewModel.loginMessage == "Connexion réussie !" {
                        navigationTarget = viewModel.userRole == "admin" ? "AdminHomePage" : "UserHomePage"
                    }
                }
                .buttonStyle(.borderedProminent)

                Text(viewModel.loginMessage)
                    .foregroundColor(viewModel.loginMessage == "Connexion réussie !" ? .green : .red)
                    .padding()

                // NavigationLink dynamique
                NavigationLink(
                    destination: destinationView(),
                    isActive: .constant(navigationTarget != nil),
                    label: { EmptyView() }
                )
                .hidden() // Cache le lien pour le rendre invisible
            }
            .navigationTitle("Connexion")
        }
    }

    @ViewBuilder
    private func destinationView() -> some View {
        if navigationTarget == "AdminHomePage" {
            AdminHomePage()
        } else if navigationTarget == "UserHomePage" {
            UserHomePage()
        } else {
            EmptyView()
        }
    }
}

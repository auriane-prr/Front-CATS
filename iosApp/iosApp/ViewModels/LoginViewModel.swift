//
//  LoginViewModel.swift
//  iosApp
//
//  Created by Auriane POIRIER on 13/12/2024.
//

import Foundation
import shared

class LoginViewModel: ObservableObject {
    private let repository: LoginRepository

    @Published var loginMessage: String = ""
    @Published var userRole: String = ""

    init() {
        let client = HttpClientFactoryImpl().create()
        self.repository = LoginRepository(client: client)
    }

    func login(email: String) {
        Task { @MainActor in // Exécution sur le Main Thread
            do {
                let user = try await repository.login(email: email)
                if let user = user {
                    self.userRole = user.role
                    self.loginMessage = "Connexion réussie !"
                } else {
                    self.loginMessage = "Email incorrect ou erreur serveur."
                }
            } catch {
                self.loginMessage = "Erreur : \(error.localizedDescription)"
            }
        }
    }
}

